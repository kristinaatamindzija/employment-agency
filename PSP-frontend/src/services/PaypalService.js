import axios from 'axios';


class PaypalService{

    getMerchant(merchantId){
        return axios.get(`${process.env.VUE_APP_API_GATEWAY}/paypal/merchant/${merchantId}`);
    }

    createTransaction(transaction){
        return axios.post(`${process.env.VUE_APP_API_GATEWAY}/paypal/transaction`, transaction);
    }

    updateTransaction(transaction){
        return axios.put(`${process.env.VUE_APP_API_GATEWAY}/paypal/transaction`, transaction);
    }

    getPlanId(merchantId, productId){
        return axios.get(`${process.env.VUE_APP_API_GATEWAY}/paypal/plan/${merchantId}/${productId}`);
    }
}
export default new PaypalService()