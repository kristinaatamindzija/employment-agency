<template>
    <div class="container">
        <h1>AAA</h1>
        <div ref="paypal"></div>
    </div>
</template>

<script>

import UserService from "../services/UserService";

export default {
    name: "PaypalPage",
    data() {
        return {
            isExpired: false,
            username: ""
        };
    },
    mounted() {
        this.isExpired = UserService.isExpired();
        console.log(this.isExpired);
        if (!this.isExpired) {
            this.username = UserService.getUsername();
        }
        this.createButton();
    },
    methods: {
        createButton() {
            const script = document.createElement("script");
            console.log(process.env)
            let aa = process.env.PAYPAL_CLIENT_ID
            let bb = '${process.env.PAYPAL_CLIENT_ID}'
            console.log(aa);
            console.log(bb);
            script.src =
                "https://www.paypal.com/sdk/js?client-id="+`${process.env.PAYPAL_CLIENT_ID}`+"&currency=EUR";
            script.addEventListener("load", this.initPaypal);
            document.body.appendChild(script);
        },

        initPaypal(){
            window.paypal.Buttons({
                createOrder: (data, actions) => {
                    return actions.order.create({
                        purchase_units: [
                            {
                                amount: {
                                    value: "10.00"
                                }
                            }
                        ]
                    });
                },
                onApprove: async (data, actions) => {
                    const order = await actions.order.capture();
                    console.log(order);
                },
                onError: err => {
                    console.log(err);
                }
            }).render(this.$refs.paypal);
        }
    },
    components: { }
}

</script>