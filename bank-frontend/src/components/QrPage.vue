<template>
    <div class="container">
        
    </div>
</template>

<script>

import Swal from 'sweetalert2'
import { Buffer } from 'buffer';
import QrService from "../services/QrService";

export default {
    name: "QrPage",
    data() {
        return {
            payeeName: "Test",
            payeeAccountNumber: "123456789",
            orderUuid: this.$route.query.merchantOrderId,
            merchantUuid: this.$route.query.merchantUuid,
            amount: this.$route.query.amount,
        }
    },
    mounted() {
        this.qrCode();
    },
    methods: {
        async qrCode() {
            const { value: formValues } = await Swal.fire({
                title: 'Fill in the fields',
                html:
                    '<input id="swal-input1" placeholder="Data on the payer" class="swal2-input">' +
                    '<input id="swal-input2" placeholder="Payment purpose" class="swal2-input">'+
                    '<input id="swal-input3" placeholder="Reference credit number" class="swal2-input">',
                focusConfirm: false,
                allowOutsideClick:false,
                preConfirm: () => {
                    return [
                    document.getElementById('swal-input1').value,
                    document.getElementById('swal-input2').value,
                    document.getElementById('swal-input3').value
                    ]
                }
                })

                if (formValues) {
                    let dto={
                    K: "PR",
                    V: "01",
                    C: "1",
                    R: this.payeeAccountNumber,
                    N: this.payeeName,
                    I: "RSD"+120*this.$route.query.amount+",00",
                    P: formValues[0],
                    SF: "189",
                    S: formValues[1],
                    RO: "00"+ formValues[2],
                }

                let transaction = {
                        status: "PENDING",
                        timestamp: new Date(),
                        merchantUuid: this.merchantUuid,
                        productUuid: this.orderUuid,
                        payerId: "123456789", //prepraviti
                    };
                console.log(transaction)

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

                            QrService.validateQrCode(formData).then(response => {
                                Swal.fire({
                                title: 'Scan the QR code',
                                text: response.data.t,
                            })
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
}

</script>