<template>
    <div class="container">
        
    </div>
</template>

<script>

import Swal from 'sweetalert2'
import { Buffer } from 'buffer';
import QrService from "../services/QrService";
import jwtDecode from 'jwt-decode'

export default {
    name: "QrPage",
    data() {
        return {
            decodedToken: "",
            payeeName: "Test",
            payeeAccountNumber: "123456789",
            orderUuid: this.$route.query.merchantOrderId,
            merchantUuid: this.$route.query.merchantUuid,
            amount: this.$route.query.amount,
        }
    },
    mounted() {
        const token = this.$route.query.token;
        this.decodedToken = jwtDecode(token);
        this.qrCode();
    },
    methods: {
        async qrCode() {

            const {data} = await QrService.getAccountCredentials(this.decodedToken.merchantId);

            let dto = {
                K: "PR",
                V: "01",
                C: "1",
                R: data.accountNumber,
                N: data.name,
                I: "RSD"+117.6*this.decodedToken.amount+",00",
                P: this.decodedToken.cardHolderName,
                SF: "221",
                S: this.decodedToken.paymentPurpose,
            }

            QrService.generateQrCode(dto).then(response => {
                console.log(response.data);
                let base64ImageString = Buffer.from(response.data, 'binary').toString('base64')

                Swal.fire({
                    title: 'QR Code',
                    showCancelButton: true,
                    confirmButtonText: 'Scan',
                    html: `<img src="data:image/png;base64,${base64ImageString}">`,
                    imageWidth: 150,
                    imageHeight: 150,
                    imageAlt: 'Custom image',
                }).then((e)=>{
                    if(e.isConfirmed){

                        var binaryImage = atob(base64ImageString);
                        var arrayBuffer = new ArrayBuffer(binaryImage.length);
                        var uint8Array = new Uint8Array(arrayBuffer);
                        for (var i = 0; i < binaryImage.length; i++) {
                        uint8Array[i] = binaryImage.charCodeAt(i);
                        }
                        var file = new File([uint8Array], 'qrcode.png', { type: 'image/png' });
                        var formData = new FormData();
                        formData.append('file', file);

                        QrService.validateQrCode(formData).then(async (response) => {
                            const {data} = await QrService.executeQrTransaction({
                                pan: this.decodedToken.pan,
                                securityCode: this.decodedToken.securityCode,
                                cardHolderName: this.decodedToken.cardHolderName,
                                validThru: this.decodedToken.validThru,
                                paymentId: this.$route.params.id,
                                qr: true,
                            })
                            Swal.fire({
                            title: 'QR code scanned successfully!',
                            text: response.data.t,
                            })
                            setTimeout(() => window.location.href = data.redirectionUrl, 5000)
                        })                
                    }
                    console.log(e)}
                    )
            }).catch(error => {
                console.log(error);
            });
        }
    }
}

</script>