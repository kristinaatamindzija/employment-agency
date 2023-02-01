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

    addPayPal(paypal) {
        return axios.post(`${process.env.VUE_APP_API_GATEWAY}/paypal/addPayPal`, {
            merchantUuid: paypal?.merchantUuid,
            merchantPaypalId: paypal?.merchantPaypalId,
            email: paypal?.email
        })
    }

    deletePayPal(merchantUuid){
        return axios.delete(`${process.env.VUE_APP_API_GATEWAY}/paypal/deletePayPal/${merchantUuid}`)
    }
}
export default new PaypalService()