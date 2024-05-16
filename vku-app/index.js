const express = require("express");
const cors = require("cors");
const axios = require("axios");
const cheerio = require("cheerio");
// const { db } = require("./firebase"); // Assuming you have set up Firebase and exported db from firebase.js
const app = express();
const router = express.Router();
const { OAuth2Client } = require('google-auth-library');
// const googleClientId = '580572019975-o5vve2ugtdatl86r296lrj8gjm79t90p.apps.googleusercontent.com';
// const googleClient = new OAuth2Client(googleClientId);

// app.get('/google-login', async (req, res) => {
//   try {
//     const { tokenId } = "eyJhbGciOiJSUzI1NiIsImtpZCI6ImEzYjc2MmY4NzFjZGIzYmFlMDA0NGM2NDk2MjJmYzEzOTZlZGEzZTMiLCJ0eXAiOiJKV1QifQ.eyJpc3MiOiJodHRwczovL2FjY291bnRzLmdvb2dsZS5jb20iLCJhenAiOiI1ODA1NzIwMTk5NzUtanE2Z2NoMnR1ZTBscjBhbDVsdGJiMTNwaDNpbWJ0OGUuYXBwcy5nb29nbGV1c2VyY29udGVudC5jb20iLCJhdWQiOiI1ODA1NzIwMTk5NzUtbzV2dmUydWd0ZGF0bDg2cjI5Nmxyajhnam03OXQ5MHAuYXBwcy5nb29nbGV1c2VyY29udGVudC5jb20iLCJzdWIiOiIxMDAwMzUwMTk2NjczMDM0ODM5MzEiLCJlbWFpbCI6InBoYW5taW5oZ2lhaHV5MTBAZ21haWwuY29tIiwiZW1haWxfdmVyaWZpZWQiOnRydWUsIm5hbWUiOiJIdXkgUGhhbiIsInBpY3R1cmUiOiJodHRwczovL2xoMy5nb29nbGV1c2VyY29udGVudC5jb20vYS9BQ2c4b2NMZVVJR3hBZDlhamVRdnQyNHUxVGUzSm1TcGFVa0d6NmVRN2lUMDJsTnBKMUZYbVgwTj1zOTYtYyIsImdpdmVuX25hbWUiOiJIdXkiLCJmYW1pbHlfbmFtZSI6IlBoYW4iLCJpYXQiOjE3MTU2MjA0MzQsImV4cCI6MTcxNTYyNDAzNH0.Axustgoc6293r4LXA4NJXGlz-eEPhfWFfetwYKnMZbBo1yAUDjjj25Tdu4Wtv8ICZyYpHuYsfjtGPLA6trVi3Tbm0Litnvuv4NvrXo6xWrPaGeeu_Ioy2tgwqufGZ4zY2Sly5I-PJ9ZQASaZi1JvO_m_uqBont_7I4wc-GQyiUuL3KL7pdO-gcaEYa3Ioy9W8gxFEA_SkNt7dU1a_WIEKiPs15NMb-6wB7h-BnW_ZGEjti02rlklxmMEOh1T0J1lEDn-Di34zIAWBNoOkpYmfmpTkmwyhhRWJpOt7IwusUDB3NlPMYUxbv60m1NFMMa7NFb0YGQZ7onLHHdR2Ux_rg";


//       // Xác thực token ID từ Google
//       const ticket = await googleClient.verifyIdToken({
//           idToken: tokenId,
//           audience: googleClientId
//       });
//       const payload = ticket.getPayload();

//       // Lấy thông tin người dùng từ payload
//       const email = payload.email;
//       const fullName = payload.name;
//       const profileImage = payload.picture;
//       const uid = payload.uid;

//       // Kiểm tra xem người dùng đã tồn tại trong Firebase Authentication chưa
//       let userRecord = await firebaseAdmin.auth().getUserByEmail(email);
      
//       // Nếu người dùng chưa tồn tại, tạo mới
//       if (!userRecord) {
//           // Tạo người dùng mới trên Firebase Authentication
//           userRecord = await firebaseAdmin.auth().createUser({
//               email: email,
//               displayName: fullName,
//               photoURL: profileImage
//           });
//       }

//       // Trả về phản hồi thành công với thông tin người dùng
//       res.status(200).json({ success: true, message: 'Login with Google successful', user: userRecord });
//   } catch (error) {
//       console.error('Error logging in with Google:', error);
//       res.status(500).json({ success: false, message: 'Login with Google failed', error: error.message });
//   }
// });

module.exports = router;

app.use(cors());

const crawlAndSaveData = async () => {
  const urls = [
    "https://daotao.vku.udn.vn/vku-thong-bao-chung",
    "https://daotao.vku.udn.vn/vku-thong-bao-khtc",
    "https://daotao.vku.udn.vn/vku-thong-bao-ktdbcl",
    "https://daotao.vku.udn.vn/vku-thong-bao-ctsv"
  ];

  let allItems = [];

  // Loop through each URL and crawl data
  for (const url of urls) {
    try {
      const response = await axios.get(url);
      const $ = cheerio.load(response.data);
      let items = [];

      // Crawl data from the webpage and push to items array
      $(
        "body > div.container.body-content > div > div > div.col-md-9 > div > div:nth-child(1) > div > div > div.item-list > ul > li "
      ).each((i, element) => {
        const linkTitle = $(element).find("a").text().trim();
        const linkHref = $(element).find("a").attr("href");
        let spanText = $(element).find("span").text().trim();
  
        // Extract the date part from spanText using regex
        const dateMatch = spanText.match(/\d{2}-\d{2}-\d{4}/);
        if (dateMatch) {
          spanText = dateMatch[0];
        } else {
          spanText = ""; // Handle case where no date is found
        }
        items.push({
          title: linkTitle,
          href: linkHref,
          spanText: spanText,
        });
      });

      


      // Concatenate items to allItems array
      allItems = allItems.concat(items);
    } catch (error) {
      console.error("Error crawling data:", error);
    }
  }

  // Store allItems in a global variable
  global.crawledItems = allItems;

  // Return allItems
  return allItems;
};

app.get("/crawlAndSaveData", async (req, res) => {
  try {
    const data = await crawlAndSaveData();
    res.status(200).json(data);
  } catch (error) {
    console.error("Error crawling and saving data:", error);
    res.status(500).json({ success: false, error: "Internal server error" });
  }
});
app.get("/search", async (req, res) => {
  const data = await crawlAndSaveData();
  const query = req.query.title;
  if (!query || !global.crawledItems || data == null) {
    return res.status(400).send("Bad Request: Missing query parameter or no data available");
  }

  const filteredItems = data.filter(item =>
    item.title.toLowerCase().includes(query.toLowerCase())
  );

  res.json(filteredItems);
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
      let spanText = $(element).find("span").text().trim();

      // Extract the date part from spanText using regex
      const dateMatch = spanText.match(/\d{2}-\d{2}-\d{4}/);
      if (dateMatch) {
        spanText = dateMatch[0];
      } else {
        spanText = ""; // Handle case where no date is found
      }
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
      let spanText = $(element).find("span").text().trim();

      // Extract the date part from spanText using regex
      const dateMatch = spanText.match(/\d{2}-\d{2}-\d{4}/);
      if (dateMatch) {
        spanText = dateMatch[0];
      } else {
        spanText = ""; // Handle case where no date is found
      }

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

    const dateMatch = spanText.match(/\d{2}-\d{2}-\d{4}/);
      if (dateMatch) {
        spanText = dateMatch[0];
      } else {
        spanText = ""; // Handle case where no date is found
      }


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

    const dateMatch = spanText.match(/\d{2}-\d{2}-\d{4}/);
      if (dateMatch) {
        spanText = dateMatch[0];
      } else {
        spanText = ""; // Handle case where no date is found
      }


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

app.get("/", (req, res) => {
  res.send('<a href="/auth/google">Authen with Google</a>')
})



app.listen(process.env.PORT || 3000, () => {
  console.log("Server is running on port 3000");
});



