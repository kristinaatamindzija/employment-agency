<template>
    <div class="container">
        <br><br><br>
        <div ref="paypal"></div>
    </div>
</template>

<script>

import PaypalService from "@/services/PaypalService";
import Swal from 'sweetalert2'

export default {
    name: "PaypalSubscriptionPage",
    data() {
        return {
            isExpired: false,
            planId: "P-82T63888Y87914132MOPPK7Y",
            merchantUuid: this.$route.query.merchantUuid,
            productId: this.$route.query.productId,
            orderUuid: this.$route.query.orderUuid,
        };
    },
    mounted() {
        this.getPlanId();
    },
    methods: {
        getPlanId(){
            PaypalService.getPlanId(this.merchantUuid, this.productId).then(response => {
                console.log(response.data);
                this.planId = response.data;
                this.createButton();
            }).catch(error => {
                console.log(error);
            })
        },
        createButton() {
            const script = document.createElement("script");
            script.src =
                "https://www.paypal.com/sdk/js?client-id="+`${process.env.VUE_APP_PAYPAL_CLIENT_ID}`+"&vault=true&intent=subscription&disable-funding=credit,card";
            script.addEventListener("load", this.initPaypal);
            document.body.appendChild(script);
        },

        initPaypal(){
            const planId = this.planId
            window.paypal.Buttons({
                style: {
                    color: 'blue',
                    shape: 'pill',
                    label: 'subscribe',
                    height: 40
                },
                createSubscription: function (data, actions) {
                    return actions.subscription.create({
                        'plan_id': planId
                    });
                },
                onApprove: async (data, actions) => {
                    console.log(actions.subscription.get())
                    const order = await actions.subscription.get();
                    console.log(order)
                    Swal.fire( 'Success!', 'You are subscribed!', 'success')
                    let transaction = {
                        status: "SUCCESS",
                        timestamp: new Date(),
                        merchantUuid: this.merchantUuid,
                        productUuid: this.orderUuid,
                        payerId: order.subscriber.payer_id,
                    };
                    PaypalService.updateTransaction(transaction).then(response => {
                        console.log(response.data);
                        window.location.replace(response.data);
                    }).catch(error => {
                        console.log(error);
                    });
                },
                onError: err => {
                    console.log(err);
                    Swal.fire( 'Error!', 'Error occured!', 'error')
                }
            }).render(this.$refs.paypal);
        }
    },
    components: { }
}

</script>