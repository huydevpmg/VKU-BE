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

async function fetchBlogListAndSaveToFirebase(url, collectionName) {
  try {
    const { data } = await axios.get(url);
    const $ = cheerio.load(data);
    const items = [];

    $(
      // "body > div.container.body-content > div > div > div.col-md-9 > div > div:nth-child(1) > div > div > div.item-list > ul > li "
      "div.item-list > ul > li "
    ).each((i, element) => {
      const linkText = $(element).find("a").text().trim();
      const linkHref = $(element).find("a").attr("href");
      const spanText = $(element).find("span").text().trim();
      items.push({
        id: i + 1,
        content: {
          text: linkText,
          href: linkHref,
          spanText: spanText,
        },
      });
    });
    // Lưu dữ liệu vào collection tương ứng trong Firestore 
    console.log(`Data crawled and saved for ${url}. Fetching for each individual blog!`);
    
    for (let item of items) {
      fetchSingleBlogSave(item.content.href)
    }
    for (let item of items) 
      await addDataToFirebase(item, collectionName);
  } catch (error) {
    console.error(`Error fetching list for ${url}:`, error);
  }
}

async function fetchSingleBlogSave(url) {
  try {
    const { data } = await axios.get(baseURL + url);
    const $ = cheerio.load(data);
    const $content = $("#content-wrapper")
    const title = $content.find("#content-head span").text()
    const metadata = $content.find(`#content-body .submitted span`).html()
    const body = $content.find(`#content-body article div[property="content:encoded"]`).html()
    const item = {
      id: url,
      content: {
        title,
        metadata,
        body
      }
    }
    await addDataToFirebase(item, `blogs`)
  } catch (error) {
    console.error(`Error fetching blog for ${url}:`, error)
  }
}

async function resetDatabase(collectionName) {
  try {
    const collectionRef = database.collection(collectionName);
    await collectionRef.set({});
  } catch (error) {
    console.error(
      `Error erasing database's collection ${collectionName}:`,
      error
    );
  }
}

async function addDataToFirebase(item, collectionName) {
  try {
    const collection = database.collection(collectionName);
    await collection.add(item);
    console.log(
      `Data added to collection ${collectionName}:`,
      item
    );
    fs.appendFileSync(logFileURL,
      `Data added to collection ${collectionName}:` + JSON.stringify(item)
    );
  } catch (error) {
    console.error(
      `Error adding data to collection ${collectionName}:`,
      error
    );
  }
}

// Lặp qua mỗi URL và cào dữ liệu
websiteURLs.forEach(async (url, index) => {
  const collectionSuffix = collectionSuffixes[index];
  const collectionName = `notification-${collectionSuffix}`
  await resetDatabase(collectionName)
  await fetchBlogListAndSaveToFirebase(url, collectionName);
});

module.exports = { fetchBlogListAndSaveToFirebase };
