<template>
    <div class="container">
        <div class="row">
            <div class="col-lg-3 col-md-2"></div>
            <div class="col-lg-7 col-md-8 login-box">
                <div class="forma container">
                    <h2 class="login-title">Order summary: </h2><br>
                    <label style="float: center; padding-left: 10%;">Order id: {{ this.orderUuid }}</label>
                    <label style="float: right; padding-right: 15%;"><b>{{ this.order.value }}
                            {{ this.order.currencyCode }}</b></label><br>
                    <label style="float: center; padding-left: 10%;">Shipping</label>
                    <label style="float: right; padding-right: 15%;"><b><i>Free</i></b></label>
                    <hr>
                    <label style="float: center; padding-left: 10%;">Total</label>
                    <label style="float: right; padding-right: 15%;"><b>{{ this.order.value }}
                            {{ this.order.currencyCode }}</b></label><br><br><br>


                    <div class="col-lg-12 login-title">Choose payment method <BIconCash class="login-pay"
                            style="color: turquoise;"></BIconCash>
                    </div>
                    <div class="row">
                        <div class="col"></div>
                        <div class="col">
                            <br><br><br>
                            <div v-if="paypal" ref="paypal"></div>
                            <button v-if="bankCard" @click="payWithBankCard()" class="button is-link"
                                style="width:100%; margin-bottom: 5px;">
                                <BIconWalletFill style="margin-right:3px"></BIconWalletFill> Bank card
                            </button>
                            <button v-if="bitcoin" class="button is-info" style="width:100%; margin-bottom: 5px;"
                                @click="payWithCrypto()">
                                <BIconCurrencyBitcoin style="margin-right:3px"></BIconCurrencyBitcoin> Pay with crypto
                            </button>
                            <button v-if="qrcode" @click="qrCode()" class="button is-light" style="width:100%; margin-bottom: 5px;"><img
                                    src="@/assets/qr-code.png" style="margin-right:3px"> Pay with QR code</button>
                            <button v-if="paypal" class="button is-dark" @click="redirectSubscribe()"
                                style="width:100%; margin-bottom: 15%;">Paypal Subscribe</button>
                        </div>

                        <div class="col"></div>
                    </div>
                </div>
            </div>
        </div>
    </div>
</template>

<script>

import Swal from 'sweetalert2'
import PaypalService from "@/services/PaypalService";
import PaymentService from "@/services/PaymentService";
import UserService from "@/services/UserService"
import jwtDecode from 'jwt-decode';

export default {
    name: "PaypalPage",
    data() {
        return {
            decodedToken: "",
            orderUuid: "",
            merchantUuid: "",
            productId: 0,
            order: {
                currencyCode: "USD",
                value: 0.0,
                description: "Test"
            },
            merchant: {
                merchantId: "SQK683LFHNNTN",
                email: "sb-msi0x23501552@business.example.com",
                successUrl: "",
                errorUrl: ""
            },
            bankCard: false,
            qrcode: false,
            paypal: false,
            bitcoin: false,
            paymenyMethods: [],
            bitcoinApiToken: '',
            payeeName: "Test",
            payeeAccountNumber: "123456789",
        };
    },
    mounted() {
        const token = this.$route.query.token;
        const decoded = jwtDecode(token);
        this.decodedToken = decoded;
        this.orderUuid = decoded.merchantOrderId;
        this.merchantUuid = decoded.merchantUuid;
        this.productId = decoded.productId;
        this.order.value = decoded.amount;
        this.getPaypalMerchant();
        this.getMerchant();
        this.getAccountCredentials();
    },
    methods: {
        getAccountCredentials() {
            PaymentService.getAccountCredentials(this.merchantUuid).then(response => {
                this.payeeName = response.data.name;
                this.payeeAccountNumber = response.data.accountNumber;

            }).catch(error => {
                console.log(error);
            });
        },
        getPaypalMerchant() {
            PaypalService.getMerchant(this.merchantUuid).then(response => {
                this.merchant.merchantId = response.data.merchantPaypalId;
                this.merchant.email = response.data.email;
                this.createButton();
            }).catch(error => {
                console.log(error);
            });
        },
        getMerchant() {
            UserService.getMerchant(this.merchantUuid).then(response => {
                this.merchant.successUrl = response.data.successUrl
                this.merchant.errorUrl = response.data.errorUrl
                this.paymenyMethods = response.data.paymentMethods
                this.bitcoinApiToken = response.data.bitcoinApiToken
                if (this.paymenyMethods.find(e => e.id === 1)) {
                    this.bankCard = true
                }
                if (this.paymenyMethods.find(e => e.id === 2)) {
                    this.qrcode = true
                }
                if (this.paymenyMethods.find(e => e.id === 3)) {
                    this.paypal = true
                }
                if (this.paymenyMethods.find(e => e.id === 4)) {
                    this.bitcoin = true
                }
            }).catch(error => {
                console.log(error);
            });
        },
        createButton() {
            const script = document.createElement("script");
            script.src =
                "https://www.paypal.com/sdk/js?client-id=" + `${process.env.VUE_APP_PAYPAL_CLIENT_ID}` + "&disable-funding=credit,card";
            script.addEventListener("load", this.initPaypal);
            document.body.appendChild(script);
        },
        initPaypal() {
            const currencyCode = this.order.currencyCode;
            const value = this.order.value;
            const description = this.order.description;
            const merchantId = this.merchant.merchantId;
            const email = this.merchant.email;
            const payerId = "";
            const merchantUuid = this.merchantUuid;
            const productUuid = this.orderUuid;

            window.paypal.Buttons({
                style: {
                    label: "buynow"
                },
                createOrder: (data, actions) => {
                    return actions.order.create({
                        purchase_units: [
                            {
                                ...description != null ? "description" : description,
                                amount: {
                                    currency_code: currencyCode,
                                    value: value
                                },
                                ...merchantId != null || email != null ? "payee" : {
                                    ...merchantId != null ? "merchant_id" : merchantId,
                                    ...email != null ? "email_address" : email
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
                    };
                    PaypalService.createTransaction(transaction).then(response => {
                        console.log(response.data);
                    }).catch(error => {
                        console.log(error);
                    });
                },
                onApprove: async (data, actions) => {
                    const order = await actions.order.capture();
                    console.log(order);
                    let transaction = {
                        status: "SUCCESS",
                        timestamp: new Date(),
                        merchantUuid: merchantUuid,
                        productUuid: productUuid,
                        payerId: order.payer.payer_id,
                    };
                    Swal.fire("Success!", "Transaction completed!", "success");
                    PaypalService.updateTransaction(transaction).then(response => {
                        console.log(response.data);
                        window.location.replace(response.data);
                    }).catch(error => {
                        console.log(error);
                    });
                },
                onError: () => {
                    let transaction = {
                        status: "FAILED",
                        timestamp: new Date(),
                        merchantUuid: merchantUuid,
                        productUuid: productUuid,
                        payerId: "",
                    };
                    PaypalService.updateTransaction(transaction).then(response => {
                        console.log(response.data);
                    }).catch(error => {
                        console.log(error);
                    });
                    Swal.fire("Error!", "Transaction not completed!", "error");
                }
            }).render(this.$refs.paypal);
        },
        payWithBankCard() {
            PaymentService.startPayment(this.merchantUuid, this.orderUuid, this.order.value, false);
        },
        redirectSubscribe() {
            this.$router.push({ path: "/subscription", query: { merchantUuid: this.merchantUuid, orderUuid: this.orderUuid, productId: this.productId, amount: this.order.value } });
        },
        async qrCode() {
            PaymentService.startPayment(this.merchantUuid, this.orderUuid, this.order.value, true, this.$route.query.token);
        },
        payWithCrypto() {
            const paymentInfo = {
                title: 'Crypto payment',
                priceAmount: '10',
                priceCurrency: 'EUR',
                receiveCurrency: 'DO_NOT_CONVERT',
                callbackUrl: 'https://7f9c-109-93-235-85.eu.ngrok.io/crypto/update-payment',
                successUrl: `${this.merchant.successUrl}/${this.$route.query.merchantOrderId}`,
                cancelUrl: `${this.merchant.errorUrl}/${this.$route.query.merchantOrderId}`,
                orderId: this.$route.query.merchantOrderId,
                description: '',
                merchantUuid: this.$route.query.merchantUuid,
                bitcoinApiToken: this.bitcoinApiToken

            }
            PaymentService.payWithCrypto(paymentInfo)
                .then((response) => {
                    console.log(response)
                    window.location.replace(response.data.payment_url)
                })
                .catch(error => console.log(error))
        }
    }
}

</script>