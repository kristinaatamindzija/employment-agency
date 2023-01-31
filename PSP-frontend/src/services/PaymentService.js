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
    startPayment(merchantUuid, merchantOrderId, amount, qr) {
        axios.post(`${process.env.VUE_APP_API_GATEWAY}/bank-service/payment`, {
            amount,
            qr: qr,
            merchantUuid,
            merchantOrderId
        })
            .then((response) => {
                console.log(response)
                window.location.href = response.data.paymentUrl
            })
            .catch(error => console.log(error))
    }

    generateQrCode(dto) {
        return axios.post("https://nbs.rs/QRcode/api/qr/v1/gen?lang=sr_RS", dto, {responseType: "arraybuffer"})
    }

    validateQrCode(formData){
        return axios.post('https://nbs.rs/QRcode/api/qr/v1/upload?lang=sr_RS', formData, {
            headers: {
                'Content-Type': 'multipart/form-data'
            }
        })
    }

    getAccountCredentials(merchantUuid) {
        return axios.get(`${process.env.VUE_APP_API_GATEWAY}/bank-service/payment/bankAccount/${merchantUuid}`)
    }

    createQrTransaction(transaction){
        return axios.post(`${process.env.VUE_APP_API_GATEWAY}/bank-service/payment/transaction`, transaction);
    }

    updateQrTransaction(transaction){
        return axios.put(`${process.env.VUE_APP_API_GATEWAY}/bank-service/payment/transaction`, transaction);
    }
}

export default new PaymentService()