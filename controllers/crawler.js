module.exports = { fetchAll, fetchBlog, resetDatabase, updateLocalCache};
// Import and Export
const axios = require("axios");
const cheerio = require("cheerio");
const fs = require("fs")
const database = require("./config");
// Constants and Utilities
const baseURL = "https://daotao.vku.udn.vn"
const commonPageSlug = "/vku-thong-bao-"
const collectionSuffixes = ["chung", "khtc", "ktdbcl", "ctsv"];
const suffixToName = (suffix) => `notification-${suffix}`
const collectionNames = collectionSuffixes.map(suffixToName)
collectionNames.push(`blogs`)

const logFileURL = "./debug.log"
fs.writeFileSync(logFileURL, "")

const urlToId = (url) => url.match(/thong-bao-(\d+)\.html/)[1]
const idToUrl = (id) => `/thong-bao/thong-bao-${id}.html`
const urlToCollection = (url) => {
  const parts = url.split('-')
  return `notification-${parts[parts.length-1]}`
}
// Initialization
const cacheFolderURL = "cache"
const cacheCollectionData = {}
const newCollectionData = {}
function readCacheFromLocal() {
  for (let collectionName of collectionNames) {
    cacheCollectionData[collectionName] = readCacheCollection(collectionName)
    newCollectionData[collectionName] = {}
    logPrint(`Retrieved all cache data for ${collectionName}`, "EVENT: ")
  }
}
readCacheFromLocal()

async function resetDatabase(req, res) {
  try {
    const promises = Array.from(collectionNames, async (table) => { 
      await deleteCollection(table)
    })
    await Promise.all(promises)
    res.send('Database expunged successfully');
  } catch (error) {
      res.status(500).json({ error: error.message });
  }
}

const gotAllData = false
async function fetchAll(req, res) {
  try {
    for (let suffix of collectionSuffixes) {
      const url = baseURL + commonPageSlug + suffix;
      const collectionName = suffixToName(suffix)
      await fetchBlogList(url, collectionName);
      // logPrint("Done Fetch for ", collectionName)
    }
    updateLocalCache()
    res.json(cacheCollectionData)
  } catch (error) {
    res.status(500).json(error)
  }
}

function readCacheCollection(collectionName) {
  const cacheURL = `${cacheFolderURL}/${collectionName}.json`
  return fs.existsSync(cacheURL) ? 
    JSON.parse(fs.readFileSync(cacheURL)) : {}
}

function updateLocalCache() {
  for (let collectionName of Object.keys(newCollectionData)) {
    const updatedData = {...readCacheCollection(collectionName), ...newCollectionData[collectionName]}
    const cacheURL = `${cacheFolderURL}/${collectionName}.json`
    fs.writeFileSync(cacheURL, JSON.stringify(updatedData))
    logPrint(`Cached for ${collectionName}`, "EVENT: ")
    newCollectionData[collectionName] = {}
  }
}

async function fetchBlog(req, res) {
  try {
    // fetchAll()
    const id = req.params["id"];
    const data = await fetchBlogById(id)
    res.json(data)
  } catch (error) {
    res.status(500).json(error)
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
    logPrint(`Fetched list of announcements from ${url} (${annoucementList.length} items). Fetching for each individual blog!`, "EVENT: ");
    
    // Fetch blogs silmutaneously!
    const promises = Array.from(annoucementList, async (entry) => {
      const {id,data} = entry
      await addDataToFirebase(collectionName, id, data);
      await fetchBlogById(id)
    })
    const results = await Promise.all(promises)
    logPrint(`Fetched all blogs in ${collectionName} (${results.length})`, "EVENT: ")
  } catch (error) {
    logError(error, `fetching list for ${url}:`);
  }
}

async function fetchBlogById(id) {
  try {
    const {exists, data: blogData} = await checkDataExist(`blogs`, id) 
    if (exists) {
      logPrint(`Blog ${id} already exists in blogs`, "INFO: ")
      return blogData;
    }
    const { data } = await axios.get(baseURL + idToUrl(id));
    const $ = cheerio.load(data);
    const $content = $("#content-wrapper")
    const title = $content.find("#content-head span").text()
    const metadata = $content.find(`#content-body .submitted span`).html()
    const body = $content.find(`#content-body article div[property="content:encoded"]`).html()
    const blog = { title, metadata, body }
    await addDataToFirebase(`blogs`, id, blog)
    return blog
  } catch (error) {
    logError(error, `fetching blog for ${id}`)
  }
}

async function deleteCollection(collectionName, batchSize = 10) {
  try {
    const collectionRef = database.collection(collectionName);
    const query = collectionRef.orderBy('__name__').limit(batchSize);
    logPrint(`Issued Deletion of ${collectionName}`, "EVENT: ")
    return new Promise((resolve, reject) => {
      deleteQueryBatch(database, query, resolve).catch(reject);
    });
  } catch (error) {
    logError(error, `deleting collection ${collectionName}`)
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
  if (cacheCollectionData?.collectionName?.key != undefined) {
    logPrint(`Cache hit for ${collectionName}/${key}`, "INFO: ")
    return {exists: true, data: cacheCollectionData[collectionName][key]}
  }
  if (newCollectionData?.collectionName?.key != undefined) {
    logPrint(`Session cache hit for ${collectionName}/${key}!`, "INFO: ")
    return {exists: true, data: newCollectionData[collectionName][key]}
  }
  const docRef = database.collection(collectionName).doc(key)
  const doc = await docRef.get()
  if (doc.exists) {
    newCollectionData[collectionName][key] = doc.data()
    return {exists: true, data: newCollectionData[collectionName][key]}
  }
  return {exists: false, data: {}}
}

async function addDataToFirebase(collectionName, key, value, forceUpdate = false) {
  try {
    key = key.toString()
    const {exists, data} = await checkDataExist(collectionName, key)
    if (exists && !forceUpdate) {
      logPrint(`Entry ${key} already exists in ${collectionName}`, "INFO: ")
      return false;
    }
    const docRef = database.collection(collectionName).doc(key);
    await docRef.set(value);
    logPrint(`Data added to Firebase collection ${collectionName}/${key}:` + JSON.stringify(value), "EVENT: ")
    newCollectionData[collectionName][key] = value
    // return value
  } catch (error) {
    logError(error, `adding to ${collectionName}`)
  }
}

function logPrint(msg, prefix = "") {
  msg = prefix + msg
  console.log(msg)
  fs.appendFileSync(logFileURL, msg + '\n')
}

function logError(error, interruptedAction = "") {
  console.error(
    `Error ${error.name} while ${interruptedAction}. Cause: ${error.cause}`
  );
  fs.appendFileSync(logFileURL,
    `ERROR ${error.name} (while ${interruptedAction}). Cause ${error.cause}. Details: ` + JSON.stringify(error) + '\n'
  );
}