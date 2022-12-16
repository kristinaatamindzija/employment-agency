<template>
    <div>
        <b-navbar toggleable="lg" type="dark" variant="info">
            <b-navbar-brand @click="$router.push('/payment')">Home</b-navbar-brand>

            <b-navbar-toggle target="nav-collapse"></b-navbar-toggle>

            <b-collapse id="nav-collapse" is-nav>
                <b-navbar-nav>
                    <b-nav-item @click="$router.push('/payment')">Payment</b-nav-item>
                </b-navbar-nav>
                <b-navbar-nav class="ml-auto">
                    <b-nav-item right @click="$router.push('/')">Sign out</b-nav-item>
                </b-navbar-nav>
            </b-collapse>
        </b-navbar>
        <div class="row" style="margin-top: 3em;">
            <div class="col"></div>
            <div class="col">
                <b-card style="margin-top: 2em;" overlay
                    img-src="https://cdn0.erstegroup.com/gemlip/v1/dam/3C6faUk1iFLX1inEfBjsiW3UZuVA/rs/ebs/www_erstebank_rs/stanovnistvo/kartice/w1200_0_0_0_0_0_m_1475164656805.kreditne_kartice_crna.jpg"
                    img-alt="Card Image" text-variant="white">
                    <b-form-checkbox @change="setSaveVisible()" v-model="bankCard" style="font-weight: bold;" switch size="lg">Bank card</b-form-checkbox>
                </b-card>
                <b-card style="margin-top: 2em;" overlay
                    img-src="https://www.merchantmaverick.com/wp-content/uploads/2021/06/qr-code.jpg"
                    img-alt="Card Image" text-variant="white">
                    <b-form-checkbox @change="setSaveVisible()" v-model="qrCode" style="font-weight: bold;" switch size="lg">QR code</b-form-checkbox>
                </b-card>
            </div>
            <div class="col">
                <b-card style="margin-top: 2em; height: 14em;" overlay
                    img-src="https://teamline.lu/wp-content/uploads/2022/04/PayPal.jpg" img-alt="Card Image"
                    text-variant="white">
                    <b-form-checkbox @change="setSaveVisible()" v-model="payPal" switch size="lg"></b-form-checkbox>
                </b-card>
                <b-card style="margin-top: 2em;" overlay img-src="https://assets.rbl.ms/29299770/origin.jpg"
                    img-alt="Card Image" text-variant="white">
                    <b-form-checkbox @change="setSaveVisible()" v-model="bitcoin" style="font-weight: bold;" switch size="lg">Bitcoin</b-form-checkbox>
                </b-card>
                <br>
                <b-button v-if="saveVisible" class="float-right" type="reset" @click="save()" variant="primary">Save</b-button>
            </div>
            <div class="col"></div>
        </div>
    </div>
</template>

<script>

export default {
    name: "PaymentPage",
    data() {
        return {
            bankCard: false,
            qrCode: false,
            payPal: false,
            bitcoin: false,
            saveVisible: false
        };
    },
    methods: {
        save() {
            this.saveVisible = false;
        },
        setSaveVisible() {
            this.saveVisible = true;
        }
    },
    components: {  }
}

</script>