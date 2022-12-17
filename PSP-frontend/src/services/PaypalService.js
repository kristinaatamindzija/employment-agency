import axios from 'axios';


class PaypalService{

    getMerchant(merchantId){
        return axios.get(`${process.env.VUE_APP_API_GATEWAY}/paypal/merchant/${merchantId}`);
    }
}
export default new PaypalService()