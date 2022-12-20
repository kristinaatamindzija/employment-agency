import axios from 'axios';

class PaymentService {
    addPayment(payment) {
        return axios.post(`${process.env.VUE_APP_API_GATEWAY}/auth/addPaymentMethod`, {
            paymentMethodId: payment?.paymentMethodId,
            merchantUuid: payment?.merchantUuid,
            merchantId: payment?.merchantId,
            merchantPassword: payment?.merchantPassword
        })
    }
    deletePayment(payment) {
        return axios.post(`${process.env.VUE_APP_API_GATEWAY}/auth/deletePaymentMethod`, {
            paymentMethodId: payment?.paymentMethodId,
            merchantUuid: payment?.merchantUuid,
            merchantId: payment?.merchantId,
            merchantPassword: payment?.merchantPassword
        })
    }
    startPayment() {
        axios.post(`${process.env.VUE_APP_API_GATEWAY}/bank-service/payment`, {
            amount: 300,
            qr: false,
            merchantUuid: '12345678-1234-1234-1234-123456789012'
        })
            .then((response) => {
                console.log(response)
                window.location.href = response.data.paymentUrl
            })
            .catch(error => console.log(error))
    }
}

export default new PaymentService()