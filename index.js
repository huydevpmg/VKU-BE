const express = require("express");
const cors = require("cors");
const { fetchData, saveDataToFirebase } = require("./controllers/crawler"); // Import crawler functions
const app = express();
app.use(express.json());
app.use(cors());

app.get("/crawl", async (req, res) => {
  const url = req.query.url;
  fetchData(url)
    .then((data) => {
      saveDataToFirebase(data);
      res.send({ msg: "Data is being processed and saved." });
    })
    .catch((error) => {
      console.error("Crawl failed:", error);
      res.status(500).send({ error: "Failed to crawl data" });
    });
});

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
app.listen(4000, () => console.log("Up & RUnning *4000"));