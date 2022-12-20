import 'bootstrap/dist/css/bootstrap.css'
import 'bootstrap-vue/dist/bootstrap-vue.css'

import Vue from 'vue';
import Vuex from 'vuex'
//import createPersistedState from 'vuex-persistedstate'
import App from './App.vue'
import axios from 'axios'
import VueAxios from 'vue-axios'
import Vuelidate from 'vuelidate'
import VueRouter from 'vue-router'

import CardPage from '@/components/CardPage'

import { BootstrapVue, IconsPlugin, FormPlugin, FormDatepickerPlugin, ToastPlugin } from 'bootstrap-vue'


Vue.config.productionTip = false
Vue.config.devtools

Vue.use(Vuex)
Vue.use(VueRouter)
Vue.use(VueAxios, axios)
Vue.use(BootstrapVue)
Vue.use(Vuelidate)

Vue.use(IconsPlugin)
Vue.use(FormPlugin)
Vue.use(FormDatepickerPlugin)
Vue.use(ToastPlugin)

const routes = [
  {
    path: '/card/:id',
    name: 'card-page',
    component: CardPage,
    children: [

    ],
  },
]

export const router = new VueRouter({
  routes,
  mode: 'history'
})

export var vue = new Vue({
  render: h => h(App),
  router,
}).$mount('#app')
