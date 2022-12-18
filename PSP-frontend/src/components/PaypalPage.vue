<template>
    <div class="container">
        <br><br><br>
        <div id="AAA" ref="paypal"></div>
    </div>
</template>

<script>

import UserService from "../services/UserService";
import Swal from 'sweetalert2'
import PaypalService from "@/services/PaypalService";

export default {
    name: "PaypalPage",
    data() {
        return {
            isExpired: false,
            username: "",
            order: {
                currencyCode: "USD",
                value: "100.00",
                description: "Test"
            },
            merchant: {
                merchantId: "SQK683LFHNNTN",
                emailAddress: "sb-msi0x23501552@business.example.com"
            },
        };
    },
    mounted() {
        this.isExpired = UserService.isExpired();
        console.log(this.isExpired);
        if (!this.isExpired) {
            this.username = UserService.getUsername();
        }
        this.getMerchant();
    },
    methods: {
        getMerchant(){
            PaypalService.getMerchant(1).then(response => {
                console.log(response.data);
                this.merchant = response.data;
                this.createButton();
            }).catch(error => {
                console.log(error);
            })
        },
        createButton() {
            const script = document.createElement("script");
            script.src =
                "https://www.paypal.com/sdk/js?client-id="+`${process.env.VUE_APP_PAYPAL_CLIENT_ID}`+"&disable-funding=credit,card";
            script.addEventListener("load", this.initPaypal);
            document.body.appendChild(script);
        },

        initPaypal(){
            const currencyCode = this.order.currencyCode
            const value = this.order.value
            const description = this.order.description
            const merchantId = this.merchant.merchantId
            const emailAddress = this.merchant.emailAddress
            console.log(currencyCode, value, description, merchantId, emailAddress)

            window.paypal.Buttons({
                style: {
                    label: 'buynow'
                },
                createOrder: (data, actions) => {
                    return actions.order.create({
                        purchase_units: [
                            {
                                ... description!=null? 'description': description,
                                amount: {
                                    currency_code: currencyCode,
                                    value: value
                                },
                                ... merchantId!=null || emailAddress!=null? 'payee':{
                                ... merchantId!=null? 'merchant_id': merchantId,
                                ... emailAddress!=null? 'email_address':emailAddress
                                },
                            }
                        ]
                    });
                },
                onClick() {
                    console.log("BBBBB")
                },
                onApprove: async (data, actions) => {
                    const order = await actions.order.capture();
                    console.log(order);
                    Swal.fire( 'Success!', 'Transaction completed!', 'success' )
                },
                onError: err => {
                    console.log(err);
                    Swal.fire( 'Error!', 'Transaction not completed!', 'error' )
                }
            }).render(this.$refs.paypal);
        }
    },
    components: { }
}

</script>