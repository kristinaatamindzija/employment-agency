import axios from 'axios';

class QrService {
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
        return axios.get(`${process.env.VUE_APP_BANK}/payment/bankCredentials/${merchantUuid}`)
    }

    createQrTransaction(transaction){
        return axios.post(`${process.env.VUE_APP_BANK}/payment/transaction`, transaction);
    }

    executeQrTransaction(transaction){
        return axios.post(`${process.env.VUE_APP_BANK}/payment/executeQr`, transaction);
    }

    updateQrTransaction(transaction){
        return axios.put(`${process.env.VUE_APP_BANK}/payment/transaction`, transaction);
    }
}

export default new QrService()