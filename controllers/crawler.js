const axios = require("axios");
const cheerio = require("cheerio");
const fs = require("fs")
const database = require("./config");

const baseURL = "https://daotao.vku.udn.vn"
const commonPageSlug = "/vku-thong-bao-"
const collectionSuffixes = ["chung", "khtc", "ktdbcl", "ctsv"];

const logFileURL = "./debug.log"
fs.writeFileSync(logFileURL, "")

// const collectionSuffixes = ["phongdaotao", "khtc", "ktdbcl", "ctsv"];

const urlToId = (url) => url.match(/thong-bao-(\d+)\.html/)[1]
const idToUrl = (id) => `/thong-bao/thong-bao-${id}.html`
const urlToCollection = (url) => {
  const parts = url.split('-')
  return `notification-${parts[parts.length-1]}`
}

const collectionData = {}

// Main Task:
const fetchAllList = async () => {
  for (let suffix of collectionSuffixes) {
    const url = baseURL + commonPageSlug + suffix;
    const collectionName = `notification-${suffix}`
    await fetchBlogList(url, collectionName);
    console.log("Done Fetch for ", collectionName)
  }
}
const dumpToLocal = () => {
  for (let table in collectionData.keys()) {
    fs.writeFileSync(`${table}.json`, JSON.stringify(collectionData[data][table]))
  }
}

async function fetchBlogList(url, collectionName = '') {
  try {
    if (collectionName === '') collectionName = urlToCollection(url)
    const { data } = await axios.get(url);
    const $ = cheerio.load(data);
    const annoucementList = [];

    $(
      // "body > div.container.body-content > div > div > div.col-md-9 > div > div:nth-child(1) > div > div > div.item-list > ul > li "
      "div.item-list > ul > li "
    ).each((i, element) => {
      const linkText = $(element).find("a").text().trim();
      const linkHref = $(element).find("a").attr("href");
      const spanText = $(element).find("span").text().trim();
      const data = {
        title: linkText,
        date: spanText,
      };
      const id = urlToId(linkHref)
      annoucementList.push({id, data})
    });
    // Lưu dữ liệu vào collection tương ứng trong Firestore 
    console.log(annoucementList.length)
    console.log(`Data crawled and saved for ${url}. Fetching for each individual blog!`);
    
    for (let entry of annoucementList) {
      const {id,data} = entry
      await addDataToFirebase(collectionName, id, data);
      await fetchSingleBlog(id)
    }
  } catch (error) {
    console.error(`Error fetching list for ${url}:`, error);
  }
}

async function fetchSingleBlog(id) {
  try {
    if (await checkDataExist('blogs', id)) {
      fs.appendFileSync(logFileURL,
        `Report: Blog ${id} already exists in blogs\n`)
      return false;
    }
    const { data } = await axios.get(baseURL + idToUrl(id));
    const $ = cheerio.load(data);
    const $content = $("#content-wrapper")
    const title = $content.find("#content-head span").text()
    const metadata = $content.find(`#content-body .submitted span`).html()
    const body = $content.find(`#content-body article div[property="content:encoded"]`).html()
    const blog = { title, metadata, body }
    await addDataToFirebase(`blogs`, id, blog)
  } catch (error) {
    console.error(`Error fetching blog for ${id}:`, error)
  }
}

async function deleteCollection(collectionName, batchSize = 10) {
  try {
    const collectionRef = database.collection(collectionName);
    const query = collectionRef.orderBy('__name__').limit(batchSize);
    
    return new Promise((resolve, reject) => {
      deleteQueryBatch(database, query, resolve).catch(reject);
    });
  } catch (error) {
    console.error(
      `Error deleting database's collection ${collectionName}:`,
      error
    );
  }
}

// Copy from https://firebase.google.com/docs/firestore/manage-data/delete-data#collections
async function deleteQueryBatch(db, query, resolve) {
  const snapshot = await query.get();

  const batchSize = snapshot.size;
  if (batchSize === 0) {
    resolve();
    return;
  }

  const batch = db.batch();
  snapshot.docs.forEach((doc) => {
    batch.delete(doc.ref);
  });
  await batch.commit();

  process.nextTick(() => {
    deleteQueryBatch(db, query, resolve);
  });
}

async function checkDataExist(collectionName, key) {
  key = key.toString()
  const docRef = database.collection(collectionName).doc(key)
  const doc = await docRef.get()
  if (doc.exists) {
    collectionData[collectionName][key] = doc.data()
    return true
  }
  return false
}

async function addDataToFirebase(collectionName, key, value, forceUpdate = false) {
  try {
    key = key.toString()
    const isExist = await checkDataExist(collectionName, key)
    if (isExist && !forceUpdate) {
      fs.appendFileSync(logFileURL,
        `Report: Entry ${key} already exists in ${collectionName}\n`)
        return false;
    }
    const docRef = database.collection(collectionName).doc(key);
    await docRef.set(value);
    collectionData[collectionName][key] = value
    fs.appendFileSync(logFileURL,
      `Report: Data added to collection ${collectionName}/${key}:` + JSON.stringify(value) + '\n'
    );
  } catch (error) {
    console.error(
      `Error ${error.name} while adding data to collection ${collectionName} cause ${error.cause}`
    );
    fs.appendFileSync(logFileURL,
      `ERROR ${error.name}. Cause ${error.cause}. Details: ` + JSON.stringify(error) + '\n'
    );
  }
}

async function resetDatabase() {
  await deleteCollection(`blogs`)
  for (let suffix in collectionSuffixes)
    await deleteCollection(`notification-${suffix}`)
}

module.exports = { fetchAllList, fetchBlogList, fetchSingleBlog, resetDatabase};
