const express = require('express')
const router = express.Router()

const crawler = require("../controllers/crawler");
// await crawler.resetDatabase()

router.get('/reset', crawler.resetDatabase)
router.get('/list', crawler.fetchAllList)
router.get('/:id', crawler.fetchSingleBlog)


// define the home page route
router.get('/', (req, res) => {
  res.send('Birds home page')
})

// define the about route
router.get('/about', (req, res) => {
  res.send('About birds')
})

module.exports = router