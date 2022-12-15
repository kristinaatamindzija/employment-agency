<template>
    <div>
        <div class="row">
            <div class="col"></div>
            <div class="col-center">
                <h1 style="margin-top: 4em; padding-left: 5em;">Login</h1>
                <b-form-group id="input-group-1" label="Username:" style="margin-top: 2em;" label-cols-sm="3"
                    label-align="right">
                    <b-form-input style="padding-left: 3em;" id="input-1" v-model="form.username"
                        required>
                    </b-form-input>
                </b-form-group>
                <b-form-group id="input-group-2" label="Password:" style="margin-top: 1em; margin-bottom: 2em;"
                    label-cols-sm="3" label-align-sm="right">
                    <b-form-input id="input-2" v-model="form.password" type="password" required></b-form-input>
                </b-form-group>
                <b-button type="submit" @click="login()"
                    style="margin-left: 7em;">Login</b-button>
                <b-button type="reset" @click="$router.push('/registration')" variant="primary"
                    style="margin-left: 12em;">Register</b-button>
            </div>
            <div class="col"></div>
        </div>
    </div>
</template>

<script>
import axios from "axios";
import { store } from "@/main";
export default {
    name: 'LoginPage',
    data() {
        return {
            form: {
                username: '',
                password: ''
            },
        }
    },
    methods: {
        async login() {
            const response = await axios.post(`${process.env.VUE_APP_API_GATEWAY}/auth/login`, {
                username: this.form.username,
                password: this.form.password
            }).catch(error => {
                this.$vs.notification({
                    title: "Error",
                    text: "Invalid username/password",
                    color: "danger",
                    position: "top-right"
                });
                throw error
            });
            store.commit("setToken", response.data?.jwt);
            store.commit("setWebShop", response.data?.webShop);
            
        }
    }
}

</script>