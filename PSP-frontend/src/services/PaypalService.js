import axios from 'axios';


class PaypalService{

    getMerchant(merchantId){
        return axios.get(`${process.env.VUE_APP_API_GATEWAY}/paypal/merchant/${merchantId}`);
    }

    createTransaction(transaction){
        return axios.post(`${process.env.VUE_APP_API_GATEWAY}/paypal/transaction`, transaction);
    }
}
export default new PaypalService()