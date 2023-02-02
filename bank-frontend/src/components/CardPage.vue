<template>
    <div>
        <div class="row">
			<div class="col-lg-3 col-md-2"></div>

			<div class="col-lg-6 col-md-8 login-box" >
                <b-form @submit="onSubmit">
                    <div class="forma container">
						<div class="col-lg-12 login-key">
							<BIconBank class="login-key" style="color: turquoise"></BIconBank>
						</div>
						<div class="col-lg-12 login-title">Enter your bank card info</div><br>
                    <b-form-group id="input-group-1" label="Pan:" label-for="input-1">
                        <b-form-input id="input-1" pattern="[0-9]+" title="please enter number only"  v-model="form.pan" required></b-form-input>
                    </b-form-group>
                    <b-form-group id="input-group-2" label="Security code:" label-for="input-2">
                        <b-form-input id="input-2" pattern="[0-9]+" title="please enter number only"  v-model="form.securityCode" required></b-form-input>
                    </b-form-group>
                    <b-form-group id="input-group-3" label="Cardholder name:" label-for="input-3">
                        <b-form-input id="input-3" v-model="form.cardHolderName" required></b-form-input>
                    </b-form-group>
                    <b-form-group id="input-group-4" label="Validthru:" label-for="input-4">
                        <b-form-input type="date" id="input-4" v-model="form.validThru" required></b-form-input>
                    </b-form-group>
                    <b-button type="submit" style="margin-bottom: 15px;" @click="executePayment()" variant="primary">Submit</b-button><br>
                    </div>
                </b-form>
            </div>
			<div class="col-lg-3 col-md-2"></div>
        </div>
    </div>
</template>

<script>
import axios from 'axios'
import "../css/main.css"
export default {
    name: "CardPage",
    data() {
        return {
            form: {
                pan: "",
                securityCode: "",
                cardHolderName: "",
                validThru: Date,
                paymentId: ""
            }
        }
    },
    mounted() {
        this.form.paymentId = this.$route.params.id
    },
    methods: {
        onSubmit(event) {
            event.preventDefault()
            alert(JSON.stringify(this.form))
        },
        async executePayment() {
            console.log(process.env.VUE_APP_BANK)
            const {data} = await axios.post(`http://localhost:8001/payment/execute`, {
                pan: this.form.pan,
                securityCode: this.form.securityCode,
                cardHolderName: this.form.cardHolderName,
                validThru: this.form.validThru,
                paymentId: this.form.paymentId
            }).catch(error => {
                this.$bvToast.toast(error, {
                    title: 'ERROR',
                    autoHideDelay: 5000,
                    position: 'top-right'
                })
                window.location.replace(data.redirectionUrl)
                throw error
            })
        
            this.$bvToast.toast('Payment successfully completed :D', {
                title: 'SUCCESS',
                autoHideDelay: 5000,
                position: 'top-right'
            })
            window.location.replace(data.redirectionUrl)
        }
    }
}

</script>