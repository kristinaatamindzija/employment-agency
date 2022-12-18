import axios from "axios";
import {store} from "@/main";
import jwt from 'jwt-decode'

export function jwtInterceptor(){
  axios.interceptors.request.use(async config => {
      const token = store.getters.token;
      if (token && !isTokenExpired(token)) {
          config.headers['Authorization'] = `Bearer ${token}`;
      } else {
        store.commit('setToken', null);
        store.commit('setWebShop', null);
      }
      return config;
  });
}

export function isTokenExpired(token) {
  if(!token) return true;
  const exp = jwt(token).exp;
  return (Date.now() >= exp * 1000)
}
