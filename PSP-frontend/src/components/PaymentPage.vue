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

        <b-modal id="modal-bank-card" v-model="showBankCardModal" no-close-on-backdrop hide-backdrop hide-header-close
            @ok="handleOkBankCard" @cancel="bankCard = !bankCard" title="Bank card">
            <b-form ref="form" @submit="saveBankCard()">
            <b-form-group id="input-group-2" type="number" label="Merchant id:" label-for="input-2">
                
                <b-form-input id="input2-id" v-model="formBankCard.merchantId" required></b-form-input>
            </b-form-group>
            <b-form-group id="input-group-2" label="Merchant password:" label-for="input-2">
                <b-form-input id="input2-pass" v-model="formBankCard.merchantPassword" type=password
                    required></b-form-input>
            </b-form-group>
            </b-form>
        </b-modal>

        <b-modal id="modal-qr-code" v-model="showQrCodeModal" no-close-on-backdrop hide-backdrop hide-header-close
            @ok="saveQrCode()" @cancel="qrCode = !qrCode" title="QR code">
            <p class="my-4">Add QR code payment?</p>
        </b-modal>

        <b-modal id="modal-paypal" v-model="showPayPalModal" no-close-on-backdrop hide-backdrop hide-header-close
            @ok="handleOkPaypal" @cancel="payPal = !payPal" title="PayPal">
            <b-form ref="form1" @submit="savePayPal()">
            <b-form-group id="input-group-3" label="Merchant paypal id:" label-for="input-3">
                <b-form-input id="input3-id" v-model="formPaypalCard.merchantPaypalId" required></b-form-input>
            </b-form-group>
            <b-form-group id="input-group-3" label="Email:" label-for="input-3">
                <b-form-input id="input3-pass" v-model="formPaypalCard.email" type=email
                    required></b-form-input>
            </b-form-group>
            </b-form>
        </b-modal>

        <b-modal id="modal-bitcoin" v-model="showBitcoinModal" no-close-on-backdrop hide-backdrop hide-header-close
            @ok="handleOkCrypto" @cancel="bitcoin = !bitcoin" title="Bitcoin">
            <b-form ref="form2" @submit="saveBitcoin()">
            <b-form-group id="input-group-4" label="Bitcoin API token:" label-for="input-4">
                <b-form-input id="input4-token" v-model="formBitcoinCard.token" required></b-form-input>
            </b-form-group>
            </b-form>
        </b-modal>
    </div>
</template>

<script>
import { store } from "@/main";
import PaypalService from "@/services/PaypalService";
import UserService from "@/services/UserService";
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
            },
            formPaypalCard: {
                merchantPaypalId: "",
                email: ""
            },
            formBitcoinCard: {
                token: ""
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
                this.webShop.paymentMethods = this.webShop.paymentMethods?.filter((e) => e.id !== 1)
                store.commit("setWebShop", this.webShop)
                this.deleteQrCode();
                this.qrCode = false;
            }
        },
        setShowQrCodeModal() {
            if (this.bankCard == true) {
                if (this.qrCode == true) {
                    this.showQrCodeModal = true
                    return
                } else {
                    this.deleteQrCode();
                    return
                }
            }
            this.qrCode = false;
        },
        setShowPayPalModal() {
            if (this.payPal == true) {
                this.showPayPalModal = true
            } else {
                PaypalService.deletePayPal(this.webShop?.merchantUuid)
                PaymentService.deletePayment({
                    paymentMethodId: 3,
                    merchantUuid: this.webShop?.merchantUuid,
                    merchantId: "",
                    merchantPassword: ""
                })
                this.webShop.paymentMethods = this.webShop.paymentMethods?.filter((e) => e.id !== 3)
                store.commit("setWebShop", this.webShop)
            }
        },
        setShowBitcoinModal() {
            if (this.bitcoin == true) {
                this.showBitcoinModal = true
            } else {
                PaymentService.deletePayment({
                    paymentMethodId: 4,
                    merchantUuid: this.webShop?.merchantUuid,
                    bitcoinApiToken: '',
                    merchantId: "",
                    merchantPassword: ""
                })
                this.webShop.paymentMethods = this.webShop.paymentMethods?.filter((e) => e.id !== 4)
                store.commit("setWebShop", this.webShop)
            }
        },
        handleOkBankCard(e) {
            e.preventDefault();
            if(document.getElementById("input2-id").value == "" || document.getElementById("input2-pass").value == ""){
                UserService.swalError("Please fill all fields");
                return;
            }
            this.saveBankCard()
        },
        handleOkPaypal(e) {
            e.preventDefault();
            if(document.getElementById("input3-id").value == "" || document.getElementById("input3-pass").value == ""){
                UserService.swalError("Please fill all fields");
                return;
            }
            this.savePaypal()
        },
        handleOkCrypto(e) {
            e.preventDefault();
            if(document.getElementById("input4-token").value == ""){
                UserService.swalError("Please fill all fields");
                return;
            }
            this.saveBitcoin()
        },
        saveBankCard() {
            PaymentService.addPayment({
                paymentMethodId: 1,
                merchantUuid: this.webShop?.merchantUuid,
                merchantId: this.formBankCard?.merchantId,
                merchantPassword: this.formBankCard?.merchantPassword
            })
            this.webShop.paymentMethods?.push({ id: 1, name: 'bank-card' })
            store.commit("setWebShop", this.webShop)
        },
        saveQrCode() {
            PaymentService.addPayment({
                paymentMethodId: 2,
                merchantUuid: this.webShop?.merchantUuid,
                merchantId: '',
                merchantPassword: ''
            })
            this.webShop.paymentMethods?.push({ id: 2, name: 'qr-code' })
            store.commit("setWebShop", this.webShop)
        },
        savePayPal() {
            PaymentService.addPayment({
                paymentMethodId: 3,
                merchantUuid: this.webShop?.merchantUuid,
                merchantId: "",
                merchantPassword: ""
            })
            PaypalService.addPayPal({
                merchantUuid: this.webShop?.merchantUuid,
                merchantPaypalId: this.formPaypalCard?.merchantPaypalId,
                email: this.formPaypalCard?.email
            })
            this.webShop.paymentMethods?.push({ id: 3, name: 'paypal' })
            store.commit("setWebShop", this.webShop)
        },
        saveBitcoin() {
            PaymentService.addPayment({
                paymentMethodId: 4,
                merchantUuid: this.webShop?.merchantUuid,
                bitcoinApiToken: this.formBitcoinCard?.token,
                merchantId: '',
                merchantPassword: ''
            })
            this.webShop.paymentMethods?.push({ id: 4, name: 'bitcoin' })
            store.commit("setWebShop", this.webShop)
        },
        deleteQrCode() {
            PaymentService.deletePayment({
                paymentMethodId: 2,
                merchantUuid: this.webShop?.merchantUuid,
                merchantId: "",
                merchantPassword: ""
            })
            this.webShop.paymentMethods = this.webShop.paymentMethods?.filter((e) => e.id !== 2)
            store.commit("setWebShop", this.webShop)
        }
    },
    components: {}
}

</script>