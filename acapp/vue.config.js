// 配置作用：让最终打包成的文件唯一
const { defineConfig } = require('@vue/cli-service')
module.exports = defineConfig({
  transpileDependencies: true,
  configureWebpack: {
    // No need for splitting
    optimization: {
      splitChunks: false
    }
  }
})