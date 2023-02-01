const { defineConfig } = require('@vue/cli-service')
const fs = require('fs')
module.exports = defineConfig({
  transpileDependencies: true,
  configureWebpack: {
    devtool: 'source-map',
  },
  devServer: {
    https: {
      key: fs.readFileSync('../cert/server-key.pem'),
      cert: fs.readFileSync('../cert/server-cert.pem'),
    },
  },
})
