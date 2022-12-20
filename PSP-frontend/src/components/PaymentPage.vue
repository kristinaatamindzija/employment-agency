<template>
    <div>
        <div class="row" style="margin-top: 3em;">
            <div class="col"></div>
            <div class="col">
                <b-card style="margin-top: 2em;" overlay
                    img-src="https://cdn0.erstegroup.com/gemlip/v1/dam/3C6faUk1iFLX1inEfBjsiW3UZuVA/rs/ebs/www_erstebank_rs/stanovnistvo/kartice/w1200_0_0_0_0_0_m_1475164656805.kreditne_kartice_crna.jpg"
                    img-alt="Card Image" text-variant="white">
                    <b-form-checkbox @change="setShowBankCardModal()" v-model="bankCard" style="font-weight: bold;"
                        switch size="lg">Bank card</b-form-checkbox>
                </b-card>
                <b-card style="margin-top: 2em; height:20px" overlay
                    img-src="https://www.merchantmaverick.com/wp-content/uploads/2021/06/qr-code.jpg"
                    img-alt="Card Image" text-variant="white">
                    <b-form-checkbox @change="setShowQrCodeModal()" v-model="qrCode" style="font-weight: bold;" switch
                        size="lg">QR code</b-form-checkbox>
                </b-card>
            </div>
            <div class="col">
                <b-card style="margin-top: 2em; height: 14em;" overlay
                    img-src="https://teamline.lu/wp-content/uploads/2022/04/PayPal.jpg" img-alt="Card Image"
                    text-variant="white">
                    <b-form-checkbox @change="setShowPayPalModal()" v-model="payPal" switch size="lg"></b-form-checkbox>
                </b-card>
                <b-card style="margin-top: 2em;" overlay img-src="https://assets.rbl.ms/29299770/origin.jpg"
                    img-alt="Card Image" text-variant="white">
                    <b-form-checkbox @change="setShowBitcoinModal()" v-model="bitcoin" style="font-weight: bold;" switch
                        size="lg">Bitcoin</b-form-checkbox>
                </b-card>
            </div>
            <div class="col"></div>
        </div>
        <b-modal id="modal-bank-card" v-model="showBankCardModal" no-close-on-backdrop hide-backdrop
            @ok="saveBankCard()" @cancel="bankCard = !bankCard" title="Bank card">
            <b-form-group id="input-group-2" label="Merchant id:" label-for="input-2">
                <b-form-input id="input-2" v-model="formBankCard.merchantId" required></b-form-input>
            </b-form-group>
            <b-form-group id="input-group-2" label="Merchant password:" type="password" label-for="input-2">
                <b-form-input id="input-2" v-model="formBankCard.merchantPassword" required></b-form-input>
            </b-form-group>
        </b-modal>
        <b-modal id="modal-qr-code" v-model="showQrCodeModal" no-close-on-backdrop hide-backdrop @ok="saveQrCode()"
            @cancel="qrCode = !qrCode" title="QR code">
            <p class="my-4">Hello from modal!</p>
        </b-modal>
        <b-modal id="modal-paypal" v-model="showPayPalModal" no-close-on-backdrop hide-backdrop @ok="savePayPal()"
            @cancel="payPal = !payPal" title="PayPal">
            <p class="my-4">Hello from modal!</p>
        </b-modal>
        <b-modal id="modal-bitcoin" v-model="showBitcoinModal" no-close-on-backdrop hide-backdrop @ok="saveBitcoin()"
            @cancel="bitcoin = !bitcoin" title="Bitcoin">
            <p class="my-4">Hello from modal!</p>
        </b-modal>
    </div>
</template>

<script>
import { store } from "@/main";
import PaymentService from "../services/PaymentService"
export default {
    name: "PaymentPage",
    data() {
        return {
            bankCard: false,
            qrCode: false,
            payPal: false,
            bitcoin: false,
            showBankCardModal: false,
            showQrCodeModal: false,
            showPayPalModal: false,
            showBitcoinModal: false,
            webShop: null,
            formBankCard: {
                merchantId: "",
                merchantPassword: ""
            }
        };
    },
    async mounted() {
        this.webShop = store.getters.webShop
        let payments = store.getters.webShop?.paymentMethods
        if (payments.find(e => e.id === 1)) {
            this.bankCard = true
        }
        if (payments.find(e => e.id === 2)) {
            this.qrCode = true
        }
        if (payments.find(e => e.id === 3)) {
            this.payPal = true
        }
        if (payments.find(e => e.id === 4)) {
            this.bitcoin = true
        }
    },
    methods: {
        setShowBankCardModal() {
            if (this.bankCard == true) {
                this.showBankCardModal = true
            } else {
                PaymentService.deletePayment({
                    paymentMethodId: 1,
                    merchantUuid: this.webShop?.merchantUuid,
                    merchantId: "",
                    merchantPassword: ""
                })
                this.webShop.paymentMethods = this.webShop.paymentMethods?.filter((e)=> e.id !== 1)
                store.commit("setWebShop", this.webShop)
            }
        },
        setShowQrCodeModal() {
            this.showQrCodeModal = true
        },
        setShowPayPalModal() {
            this.showPayPalModal = true
        },
        setShowBitcoinModal() {
            this.showBitcoinModal = true
        },
        saveBankCard() {
            PaymentService.addPayment({
                paymentMethodId: 1,
                merchantUuid: this.webShop?.merchantUuid,
                merchantId: this.formBankCard?.merchantId,
                merchantPassword: this.formBankCard?.merchantPassword
            })
            this.webShop.paymentMethods?.push({id: 1, name: 'bank-card'})
            console.log(this.webShop)
            store.commit("setWebShop", this.webShop)
        },
        saveQrCode() {

        },
        savePayPal() {

        },
        saveBitcoin() {

        }
    },
    components: {}
}

</script>