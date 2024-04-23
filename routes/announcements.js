const express = require('express')
const router = express.Router()

const crawler = require("../controllers/crawler");

router.get('/reset', crawler.resetDatabase)
router.get('/list', crawler.fetchAll)
router.get('/blog/:id', crawler.fetchBlog)
router.get('/cache', (req,res) => {
  crawler.updateLocalCache()
  res.end()
})


// define the home page route
router.get('/', (req, res) => {
  res.send('Birds home page')
})

// define the about route
router.get('/about', (req, res) => {
  res.send('About birds')
})

module.exports = router