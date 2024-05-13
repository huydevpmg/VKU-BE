const express = require("express");
const cors = require("cors");
const axios = require("axios");
const cheerio = require("cheerio");
const { db } = require("./firebase"); // Assuming you have set up Firebase and exported db from firebase.js

const app = express();
app.use(cors());

app.get("/crawlAndSaveData", async (req, res) => {
  try {
    const urls = [
      "https://daotao.vku.udn.vn/vku-thong-bao-chung",
      "https://daotao.vku.udn.vn/vku-thong-bao-khtc",
      "https://daotao.vku.udn.vn/vku-thong-bao-ktdbcl",
      "https://daotao.vku.udn.vn/vku-thong-bao-ctsv"
    ];

    const crawlAndSave = async (url, documentName) => {
      const response = await axios.get(url);
      const $ = cheerio.load(response.data);
      let items = [];

      // Crawl dữ liệu từ trang web và lưu vào mảng items
      $(
        "body > div.container.body-content > div > div > div.col-md-9 > div > div:nth-child(1) > div > div > div.item-list > ul > li "
      ).each((i, element) => {
        const linkTitle = $(element).find("a").text().trim();
        const linkHref = $(element).find("a").attr("href");
        const spanText = $(element).find("span").text().trim();

        // Thêm dữ liệu vào mảng items
        items.push({
          title: linkTitle,
          href: linkHref,
          spanText: spanText,
        });
      });

      // Lưu dữ liệu vào Firestore trong một document của subcollection
      const docRef = db.collection("Notification").doc(documentName);
      await docRef.set({ items });

      return items;
    };

    // Iterate through each URL and crawl data
    const allItems = await Promise.all(urls.map(async (url) => {
      const documentName = url.split('/').pop(); // Extract document name from URL
      return await crawlAndSave(url, documentName);
    }));

    return res.status(200).json(allItems); // Removed send() because json() sends the response automatically
  } catch (error) {
    console.error("Error crawling and saving data:", error);
    return res
      .status(500)
      .send({ status: "Failed", msg: "Failed to crawl and save data" });
  }
});



app.get("/crawlAndSaveDataDaoTao", async (req, res) => {
  try {
    const url = "https://daotao.vku.udn.vn/vku-thong-bao-chung";
    const response = await axios.get(url);
    const $ = cheerio.load(response.data);
    let items = [];

    // Crawl dữ liệu từ trang web và lưu vào mảng items
    $(
      "body > div.container.body-content > div > div > div.col-md-9 > div > div:nth-child(1) > div > div > div.item-list > ul > li "
    ).each((i, element) => {
      const linkTitle = $(element).find("a").text().trim();
      const linkHref = $(element).find("a").attr("href");
      const spanText = $(element).find("span").text().trim();

      // Thêm dữ liệu vào mảng items
      items.push({
        title: linkTitle,
        href: linkHref,
        spanText: spanText,
      });
    });
    // Lưu dữ liệu vào Firestore trong một document của subcollection
    const docRef = db.collection("Notification").doc("Daotao"); // Document reference within subcollection
    await docRef.set({ items }); // Set all items as a single field in the document
    
    return res.status(200).json(items); // Removed send() because json() sends the response automatically
  } catch (error) {
    console.error("Error crawling and saving data:", error);
    return res
      .status(500)
      .send({ status: "Failed", msg: "Failed to crawl and save data" });
  }
});

app.get("/crawlAndSaveCTSV", async (req, res) => {
  try {
    const url = "https://daotao.vku.udn.vn/vku-thong-bao-ctsv";
    const response = await axios.get(url);
    const $ = cheerio.load(response.data);
    let items = [];

    // Crawl dữ liệu từ trang web và lưu vào mảng items
    $(
      "body > div.container.body-content > div > div > div.col-md-9 > div > div:nth-child(1) > div > div > div.item-list > ul > li "
    ).each((i, element) => {
      const linkTitle = $(element).find("a").text().trim();
      const linkHref = $(element).find("a").attr("href");
      const spanText = $(element).find("span").text().trim();

      // Thêm dữ liệu vào mảng items
      items.push({
        title: linkTitle,
        href: linkHref,
        spanText: spanText,
      });
    });

    
    // Lưu dữ liệu vào Firestore trong một document của subcollection
    const docRef = db.collection("Notification").doc("CTSV"); // Document reference within subcollection
    await docRef.set({ items }); // Set all items as a single field in the document
    return res.status(200).json(items); // Removed send() because json() sends the response automatically
  } catch (error) {
    console.error("Error crawling and saving data:", error);
    return res
      .status(500)
      .send({ status: "Failed", msg: "Failed to crawl and save data" });
  }
});

app.get("/crawlAndSaveKHTC", async (req, res) => {
  try {
    const url = "https://daotao.vku.udn.vn/vku-thong-bao-khtc";
    const response = await axios.get(url);
    const $ = cheerio.load(response.data);
    let items = [];

    // Crawl dữ liệu từ trang web và lưu vào mảng items
    $(
      "body > div.container.body-content > div > div > div.col-md-9 > div > div:nth-child(1) > div > div > div.item-list > ul > li "
    ).each((i, element) => {
      const linkTitle = $(element).find("a").text().trim();
      const linkHref = $(element).find("a").attr("href");
      const spanText = $(element).find("span").text().trim();

      // Thêm dữ liệu vào mảng items
      items.push({
        title: linkTitle,
        href: linkHref,
        spanText: spanText,
      });
    });

    // Lưu dữ liệu vào Firestore trong một document của subcollection
    const docRef = db.collection("Notification").doc("KHTC"); // Document reference within subcollection
    await docRef.set({ items }); // Set all items as a single field in the document
    return res.status(200).json(items); // Removed send() because json() sends the response automatically
  } catch (error) {
    console.error("Error crawling and saving data:", error);
    return res
      .status(500)
      .send({ status: "Failed", msg: "Failed to crawl and save data" });
  }
});

app.get("/crawlAndSaveKTDBCL", async (req, res) => {
  try {
    const url = "https://daotao.vku.udn.vn/vku-thong-bao-ktdbcl";
    const response = await axios.get(url);
    const $ = cheerio.load(response.data);
    let items = [];

    // Crawl dữ liệu từ trang web và lưu vào mảng items
    $(
      "body > div.container.body-content > div > div > div.col-md-9 > div > div:nth-child(1) > div > div > div.item-list > ul > li "
    ).each((i, element) => {
      const linkTitle = $(element).find("a").text().trim();
      const linkHref = $(element).find("a").attr("href");
      const spanText = $(element).find("span").text().trim();

      // Thêm dữ liệu vào mảng items
      items.push({
        title: linkTitle,
        href: linkHref,
        spanText: spanText,
      });
    });

   // Lưu dữ liệu vào Firestore trong một document của subcollection
   const docRef = db.collection("Notification").doc("KTDBCL"); // Document reference within subcollection
   await docRef.set({ items }); // Set all items as a single field in the document

    return res.status(200).json(items); // Removed send() because json() sends the response automatically
  } catch (error) {
    console.error("Error crawling and saving data:", error);
    return res
      .status(500)
      .send({ status: "Failed", msg: "Failed to crawl and save data" });
  }
});
app.listen(process.env.PORT || 3000, () => {
  console.log("Server is running on port 3000");
});
