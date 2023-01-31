import Swal from 'sweetalert2'
import jwt from 'jwt-decode'
import axios from 'axios';
import { store } from '@/main';

class UserService{

    login(username, password){
        console.log(`${process.env.VUE_APP_API_GATEWAY}`)
        return axios.post(`${process.env.VUE_APP_API_GATEWAY}/auth/login`, {
            username: username,
            password: password
        })
    }

    parseJwt(token){
        return jwt(token);
    }

    isExpired(){
        if(store.getters.token !== null){
            let token = store.getters.token
            let expiration = this.parseJwt(token).exp
            return expiration < Date.now() / 1000
        }else{
            return true;
        }
    }

    getUsername() {
        let jwt = store.getters.token
        try {
          const content = this.parseJwt(jwt !== null ? jwt : "");
          return content.sub;
        } catch {
          alert("Error getting token, jwt is null");
          return "";
        }
    }

    swalSuccess(text) {
        const Toast = Swal.mixin({
            toast: true,
            position: 'top-end',
            showConfirmButton: false,
            timer: 3000,
            timerProgressBar: true,
            didOpen: (toast) => {
            toast.addEventListener('mouseenter', Swal.stopTimer)
            toast.addEventListener('mouseleave', Swal.resumeTimer)
            }
        })
        
        Toast.fire({
            icon: 'success',
            title: text
        })
    }

    swalError(text) {
        const Toast = Swal.mixin({
            toast: true,
            position: 'top-end',
            showConfirmButton: false,
            timer: 2000,
            timerProgressBar: true,
            didOpen: (toast) => {
            toast.addEventListener('mouseenter', Swal.stopTimer)
            toast.addEventListener('mouseleave', Swal.resumeTimer)
            }
        })
        
        Toast.fire({
            icon: 'error',
            title: text
        })
    }

    getMerchant(merchantId){
        return axios.get(`${process.env.VUE_APP_API_GATEWAY}/auth/merchantData/${merchantId}`);
    }
}

export default new UserService()