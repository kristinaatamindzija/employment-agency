<template>
    <div>
        <div class="row">
            <div class="col"></div>
            <div class="col">
                <b-form @submit="onSubmit">
                    <b-form-group id="input-group-1" label="Pan:" label-for="input-1">
                        <b-form-input id="input-1" v-model="form.pan" required></b-form-input>
                    </b-form-group>
                    <b-form-group id="input-group-2" label="Security code:" label-for="input-2">
                        <b-form-input id="input-2" v-model="form.securityCode" required></b-form-input>
                    </b-form-group>
                    <b-form-group id="input-group-3" label="Cardholder name:" label-for="input-3">
                        <b-form-input id="input-3" v-model="form.cardHolderName" required></b-form-input>
                    </b-form-group>
                    <b-form-group id="input-group-4" label="Validthru:" label-for="input-4">
                        <b-form-input type="date" id="input-4" v-model="form.validThru"
                            required></b-form-input>
                    </b-form-group>
                    <b-button type="submit" @click="executePayment()" variant="primary">Submit</b-button>
                </b-form>
            </div>
            <div class="col"></div>
        </div>
    </div>
</template>

<script>
import axios from 'axios'
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
        executePayment() {
            console.log(process.env.VUE_APP_BANK)
            return axios.post(`http://localhost:8001/payment/execute`, {
                pan: this.form.pan,
                securityCode: this.form.securityCode,
                cardHolderName: this.form.cardHolderName,
                validThru: this.form.validThru,
                paymentId: this.form.paymentId
            })
        }
    }
}

</script>