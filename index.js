const express = require("express");
const cors = require("cors");
const PORT = 4000;
const app = express();
app.use(express.json());
app.use(cors());

// await crawler.resetDatabase()
// crawler.fetchAllList()

const announcementRoutes = require('./routes/announcements')
app.use('/announcement', announcementRoutes)

const server = app.listen(PORT, () => console.log(`Up & Running at ${PORT}`));
const { updateLocalCache } = require("./controllers/crawler");
server.on("close", updateLocalCache)