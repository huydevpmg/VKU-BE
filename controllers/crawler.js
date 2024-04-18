const axios = require("axios");
const cheerio = require("cheerio");
const NotificationDaoTaoVKU = require("./config");

const websiteURLs = [
  "https://daotao.vku.udn.vn/vku-thong-bao-chung",
  "https://daotao.vku.udn.vn/vku-thong-bao-khtc",
  "https://daotao.vku.udn.vn/vku-thong-bao-ktdbcl",
  "https://daotao.vku.udn.vn/vku-thong-bao-ctsv",
];

// Mảng chứa các đuôi của URL tương ứng với tên collection
const collectionSuffixes = ["phongdaotao", "khtc", "ktdbcl", "ctsv"];

async function fetchDataAndSaveToFirebase(url, collectionSuffix) {
  try {
    const { data } = await axios.get(url);
    const $ = cheerio.load(data);
    const items = [];

    $(
      "body > div.container.body-content > div > div > div.col-md-9 > div > div:nth-child(1) > div > div > div.item-list > ul > li "
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
    await saveDataToFirebase(items, collectionSuffix);
    console.log(`Data crawled and saved for ${url}`);
  } catch (error) {
    console.error(`Error fetching data for ${url}:`, error);
  }
}

async function saveDataToFirebase(items, collectionSuffix) {
  try {
    const collectionRef = NotificationDaoTaoVKU.doc(
      `notification-${collectionSuffix}`
    );
    await collectionRef.set({ items });
    console.log(
      `Data added to collection notification-${collectionSuffix}:`,
      items
    );
  } catch (error) {
    console.error(
      `Error adding data to collection notification-${collectionSuffix}:`,
      error
    );
  }
}

// Lặp qua mỗi URL và cào dữ liệu
websiteURLs.forEach(async (url, index) => {
  const collectionSuffix = collectionSuffixes[index];
  await fetchDataAndSaveToFirebase(url, collectionSuffix);
});

module.exports = { fetchDataAndSaveToFirebase };
