<template>
    <div class="container">
        <br><br><br>
        <div ref="paypal"></div>
    </div>
</template>

<script>

import UserService from "../services/UserService";
import Swal from 'sweetalert2'

export default {
    name: "PaypalSubscriptionPage",
    data() {
        return {
            isExpired: false,
            username: "",
            planId: "P-82T63888Y87914132MOPPK7Y",
        };
    },
    mounted() {
        this.isExpired = UserService.isExpired();
        console.log(this.isExpired);
        if (!this.isExpired) {
            this.username = UserService.getUsername();
        }
        this.createButton();    //pozvati getMerchant() umesto createButton()
    },
    methods: {
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
                    Swal.fire( 'Success!', 'You are subscribed!', 'success' )
                },
                onError: err => {
                    console.log(err);
                    Swal.fire( 'Error!', 'Error occured!', 'error' )
                }
            }).render(this.$refs.paypal);
        }
    },
    components: { }
}

</script>