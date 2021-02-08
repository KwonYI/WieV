import '@babel/polyfill'
import 'mutationobserver-shim'
import Vue from 'vue'
import './plugins/axios'
import App from './App.vue'
import store from './store'
import router from './router'
import vuetify from './plugins/vuetify'

import Antd from 'ant-design-vue'
import 'ant-design-vue/dist/antd.css'
// import VueRouter from 'vue-router'
import Directives from "./plugins/directives"

Vue.use(Directives)
Vue.use(Antd)

Vue.config.productionTip = false

new Vue({
  store,
  router,
  vuetify,
  render: (h) => h(App),
}).$mount("#app");
