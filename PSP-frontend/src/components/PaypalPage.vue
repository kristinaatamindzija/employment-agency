<template>
    <div class="container">
        <div class="row">
            <div class="col"></div>
            <div class="col">
                <br><br><br>
                <div ref="paypal"></div>
                <b-card v-if="bankCard" style="margin-top: 2em" overlay
                    img-src="https://cdn0.erstegroup.com/gemlip/v1/dam/3C6faUk1iFLX1inEfBjsiW3UZuVA/rs/ebs/www_erstebank_rs/stanovnistvo/kartice/w1200_0_0_0_0_0_m_1475164656805.kreditne_kartice_crna.jpg"
                    img-alt="Card Image" text-variant="white">
                    <b-button @click="payWithBankCard()" variant="primary">Bank card</b-button>
                </b-card>
            </div>
            <div class="col"></div>
        </div>
    </div>
</template>

<script>

import UserService from "../services/UserService";
import Swal from 'sweetalert2'
import PaypalService from "@/services/PaypalService";
import PaymentService from "@/services/PaymentService";

export default {
    name: "PaypalPage",
    data() {
        return {
            isExpired: false,
            username: "",
            orderId: 1, //ovo ce se dobavljati sa bonite
            merchantId: 1, //ovo ce se dobavljati sa bonite
            order: {
                currencyCode: "USD",
                value: "100.00",
                description: "Test"
            },
            merchant: {
                merchantId: "SQK683LFHNNTN",
                email: "sb-msi0x23501552@business.example.com"
            },
            bankCard: true
        };
    },
    mounted() {
        this.isExpired = UserService.isExpired();
        if (!this.isExpired) {
            this.username = UserService.getUsername();
        }
        this.getMerchant();
        //this.getOrder(); //ovo ce dobaviti sa beka kad dobijemo id iz bonite
        this.merchantId = 1;
        this.orderId = 1;
    },
    methods: {
        getMerchant() {
            PaypalService.getMerchant(1).then(response => {
                this.merchant.merchantId = response.data.merchantPaypalId;
                this.merchant.email = response.data.email;
                this.createButton();
            }).catch(error => {
                console.log(error);
            })
        },
        createButton() {
            const script = document.createElement("script");
            script.src =
                "https://www.paypal.com/sdk/js?client-id=" + `${process.env.VUE_APP_PAYPAL_CLIENT_ID}` + "&disable-funding=credit,card";
            script.addEventListener("load", this.initPaypal);
            document.body.appendChild(script);
        },

        initPaypal() {
            const currencyCode = this.order.currencyCode
            const value = this.order.value
            const description = this.order.description
            const merchantId = this.merchant.merchantId
            const email = this.merchant.email

            const productUuid = this.orderId
            const payerId = this.username
            const merchantUuid = this.merchantId

            window.paypal.Buttons({
                style: {
                    label: 'buynow'
                },
                createOrder: (data, actions) => {
                    return actions.order.create({
                        purchase_units: [
                            {
                                ...description != null ? 'description' : description,
                                amount: {
                                    currency_code: currencyCode,
                                    value: value
                                },
                                ...merchantId != null || email != null ? 'payee' : {
                                    ...merchantId != null ? 'merchant_id' : merchantId,
                                    ...email != null ? 'email_address' : email
                                },
                            }
                        ]
                    });
                },
                onClick() {
                    let transaction = {
                        status: "PENDING",
                        timestamp: new Date(),
                        merchantUuid: merchantUuid,
                        productUuid: productUuid,
                        payerId: payerId,
                    }
                    PaypalService.createTransaction(transaction).then(response => {
                        console.log(response.data);
                    }).catch(error => {
                        console.log(error);
                    })
                },
                onApprove: async (data, actions) => {
                    const order = await actions.order.capture();
                    console.log(order);
                    let transaction = {
                        status: "SUCCESS",
                        timestamp: new Date(),
                        merchantUuid: merchantUuid,
                        productUuid: productUuid,
                        payerId: payerId,
                    }
                    PaypalService.updateTransaction(transaction).then(response => {
                        console.log(response.data);
                    }).catch(error => {
                        console.log(error);
                    })
                    Swal.fire('Success!', 'Transaction completed!', 'success')
                },
                onError: err => {
                    console.log(err);
                    let transaction = {
                        status: "FAILED",
                        timestamp: new Date(),
                        merchantUuid: merchantUuid,
                        productUuid: productUuid,
                        payerId: payerId,
                    }
                    PaypalService.createTransaction(transaction).then(response => {
                        console.log(response.data);
                    }).catch(error => {
                        console.log(error);
                    })
                    Swal.fire('Error!', 'Transaction not completed!', 'error')
                }
            }).render(this.$refs.paypal);
        },

        payWithBankCard(){
            PaymentService.startPayment(this.$route.query.merchantUuid, this.$route.query.merchantOrderId, this.$route.query.amount)
        }
    },
    components: {}
}

</script>