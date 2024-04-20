const express = require("express");
const cors = require("cors");
const crawler = require("./controllers/crawler");
const PORT = 4000;
const app = express();
app.use(express.json());
app.use(cors());

// await crawler.resetDatabase()
crawler.fetchAllList()

/*
// SIMPLE ROUTING PASSING
app.get('/example/a', (req, res) => {
  res.send('Hello from A!')
})

app.get('/example/b', (req, res, next) => {
  console.log('the response will be sent by the next function ...')
  next()
}, (req, res) => {
  res.send('Hello from B!')
})

const cb0 = function (req, res, next) {
  console.log('CB0')
  next()
}

const cb1 = function (req, res, next) {
  console.log('CB1')
  next()
}

const cb2 = function (req, res) {
  res.send('Hello from C!')
}

app.get('/example/c', [cb0, cb1, cb2])

app.get('/example/d', [cb0, cb1], (req, res, next) => {
  console.log('the response will be sent by the next function ...')
  next()
}, (req, res) => {
  res.send('Hello from D!')
})
*/

// app.get("/crawl", async (req, res) => {
//   const url = req.query.url;
//   fetchData(url)
//     .then((data) => {
//       saveDataToFirebase(data);
//       res.send({ msg: "Data is being processed and saved." });
//     })
//     .catch((error) => {
//       console.error("Crawl failed:", error);
//       res.status(500).send({ error: "Failed to crawl data" });
//     });
// });

// app.get("/", async (req, res) => {
//   const snapshot = await NotificationDaoTao.get();
//   const list = snapshot.docs.map((doc) => ({ id: doc.id, ...doc.data() }));
//   res.send(list);
// });

// app.post("/create", async (req, res) => {
//   const data = req.body;
//   await NotificationDaoTao.add({ data });
//   res.send({ msg: "Notification Added" });
// });

// app.post("/update", async (req, res) => {
//   const id = req.body.id;
//   delete req.body.id;
//   const data = req.body;
//   await NotificationDaoTao.doc(id).update(data);
//   res.send({ msg: "Updated" });
// });

// app.post("/delete", async (req, res) => {
//   const id = req.body.id;
//   await NotificationDaoTao.doc(id).delete();
//   res.send({ msg: "Deleted" });
// });
app.listen(PORT, () => console.log(`Up & Running at ${PORT}`));
