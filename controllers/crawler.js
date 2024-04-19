const axios = require("axios");
const cheerio = require("cheerio");
const fs = require("fs")

const database = require("./config");

const baseURL = "https://daotao.vku.udn.vn"
const websiteURLs = [
  baseURL + "/vku-thong-bao-chung",
  baseURL + "/vku-thong-bao-khtc",
  baseURL + "/vku-thong-bao-ktdbcl",
  baseURL + "/vku-thong-bao-ctsv",
];

const logFileURL = "./debug.log"
fs.writeFileSync(logFileURL, "")

// Mảng chứa các đuôi của URL tương ứng với tên collection
const collectionSuffixes = ["phongdaotao", "khtc", "ktdbcl", "ctsv"];

const urlToId = (url) => url.match(/thong-bao-(\d+)\.html/)[1]
const idToUrl = (id) => `/thong-bao/thong-bao-${id}.html`

async function fetchBlogListAndSaveToFirebase(url, collectionName) {
  try {
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
    console.log(`Data crawled and saved for ${url}. Fetching for each individual blog!`);
    
    for (let entry of annoucementList) {
      const {id,data} = entry
      await addDataToFirebase(collectionName, id, data);
      await fetchSingleBlogThenSave(id)
    }
  } catch (error) {
    console.error(`Error fetching list for ${url}:`, error);
  }
}

async function fetchSingleBlogThenSave(id) {
  try {
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
    // When there are no documents left, we are done
    resolve();
    return;
  }

  // Delete documents in a batch
  const batch = db.batch();
  snapshot.docs.forEach((doc) => {
    batch.delete(doc.ref);
  });
  await batch.commit();

  // Recurse on the next process tick, to avoid
  // exploding the stack.
  process.nextTick(() => {
    deleteQueryBatch(db, query, resolve);
  });
}

async function addDataToFirebase(collectionName, key, value) {
  try {
    const collection = database.collection(collectionName);
    key = key.toString()
    // await collection.add(value)
    // collection.doc('id').set({text: 'haha'})
    await collection.doc(key).set(value);
    // console.log(
    //   `Data added to collection ${collectionName}:`,
    //   item
    // );
    fs.appendFileSync(logFileURL,
      `Data added to collection ${collectionName}/${key}:` + JSON.stringify(value) + '\n'
    );
  } catch (error) {
    console.error(
      `Error ${error.name} while adding data to collection ${collectionName} cause ${error.cause}`
    );
    fs.appendFileSync(logFileURL,
      `Error ${error.name}. Cause ${error.cause}. Details: ` + JSON.stringify(error) + '\n'
    );
  }
}

// Lặp qua mỗi URL và cào dữ liệu
let blogsDeleted = false
websiteURLs.forEach(async (url, index) => {
  if (!blogsDeleted) {
    await deleteCollection(`blogs`)
    blogsDeleted = true
  }
  const collectionSuffix = collectionSuffixes[index];
  const collectionName = `notification-${collectionSuffix}`
  await deleteCollection(collectionName)
  await fetchBlogListAndSaveToFirebase(url, collectionName);
  console.log("Done for ", collectionName)
});

module.exports = { fetchBlogListAndSaveToFirebase };
