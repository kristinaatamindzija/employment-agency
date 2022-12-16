import Swal from 'sweetalert2'
import jwt from 'jwt-decode'
import axios from 'axios';

//const USER_API_BASE_URL = "http://localhost:8080/api/user";

class UserService{

    login(username, password){
        return axios.post(`${process.env.VUE_APP_API_GATEWAY}/auth/login`, {
            username: username,
            password: password
        })
    }





    parseJwt(token){
        return jwt(token);
    }

    isExpired(){
        if(JSON.parse(localStorage.getItem('vuex')) !== null){
            let token = JSON.parse(localStorage.getItem('vuex')).token
            let expiration = this.parseJwt(token).exp
            return expiration < Date.now() / 1000
        }else{
            return true;
        }
    }

    getUsername() {
        let jwt = JSON.parse(localStorage.getItem('vuex')).token
        try {
          const content = this.parseJwt(jwt !== null ? jwt : "");
          return content.sub;
        } catch {
          alert("Error getting token, jwt is null");
          return "";
        }
    }

    deleteLocalStorageIfExpired(){
        if(JSON.parse(localStorage.getItem('user')) !== null && this.isExpired()){
        Swal.fire('Error!', 'Session expired!', 'error')
        localStorage.removeItem('vuex')
        localStorage.clear()
        return true;
        }else {return false;}
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
}

export default new UserService()