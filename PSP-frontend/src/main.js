import Vue from 'vue';
import Vuex from 'vuex'
import createPersistedState from 'vuex-persistedstate'
import App from './App.vue'
import axios from 'axios'
import VueAxios from 'vue-axios'
import {jwtInterceptor} from "@/_helpers/jwt.interceptor";
import Vuelidate from 'vuelidate'
import VueRouter from 'vue-router'
import { BootstrapVue, IconsPlugin } from 'bootstrap-vue'
import 'bootstrap/dist/css/bootstrap.css'
import 'bootstrap-vue/dist/bootstrap-vue.css'
import 'bulma/css/bulma.css'
import VueSweetalert2 from 'vue-sweetalert2';

import { FormPlugin, FormGroupPlugin, NavPlugin } from 'bootstrap-vue';
import { BootstrapIconsPlugin } from 'bootstrap-icons-vue';

import LoginPage from "@/components/LoginPage"
import RegistrationPage from "@/components/RegistrationPage"
import PaymentPage from "@/components/PaymentPage"
import PaypalPage from "@/components/PaypalPage"
import Navbar from "@/components/NavbarComponent"

Vue.config.productionTip = false
Vue.config.devtools

jwtInterceptor()

Vue.use(Vuex)
Vue.use(VueRouter)
Vue.use(VueAxios, axios)
Vue.use(BootstrapVue)
Vue.use(IconsPlugin)
Vue.use(Vuelidate)

Vue.use(FormPlugin)
Vue.use(FormGroupPlugin)
Vue.use(NavPlugin)
Vue.use(BootstrapIconsPlugin)
Vue.use(VueSweetalert2);

export const store = new Vuex.Store({
    plugins: [createPersistedState()],
    state: {
      webShop: null,
      token: null,
    },
    mutations: {
      setToken(state, token) {
        state.token = token
      },
      setWebShop(state, webShop) {
        state.webShop = webShop
      },
    },
    getters: {
      token(state) {
        return state.token
      },
      webShop(state) {
        return state.webShop
      },
    }
  })
  
  const routes = [
    {
      path: '/',
      name: 'login-page',
      component: LoginPage,
      children: [
  
      ],
    },
    {
      path: '/registration',
      name: 'registration-page',
      component: RegistrationPage,
      children:[

      ]
    },
    {
      path: '/payment',
      name: 'payment-page',
      component: PaymentPage,
      children:[

      ]
    },
    {
      path: '/paypal',
      name: 'paypal-page',
      component: PaypalPage,
      children:[]

    },
    {
      path: '/navbar',
      name: 'navbar',
      component: Navbar,
      children:[

      ]
    }
  ]
  
  export const router = new VueRouter({
    routes,
    mode: 'history'
  })
  
  export var vue = new Vue({
    render: h => h(App),
    router,
    store,
  }).$mount('#app')