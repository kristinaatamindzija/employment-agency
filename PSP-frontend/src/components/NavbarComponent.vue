<template>
    <div>
        <nav class="navbar" role="navigation" aria-label="main navigation" style="background-image: linear-gradient(to bottom left, PaleTurquoise, MintCream)">
            <div class="navbar-brand">
                <a class="navbar-item" @click="$router.push('/')">
                <img src="https://bulma.io/images/bulma-logo.png" width="112" height="28">
                </a>

                <a role="button" class="navbar-burger" aria-label="menu" aria-expanded="false" data-target="navbarBasicExample">
                <span aria-hidden="true"></span>
                <span aria-hidden="true"></span>
                <span aria-hidden="true"></span>
                </a>
            </div>

            <div id="navbarBasicExample" class="navbar-menu">
                <div class="navbar-start">
                <a class="navbar-item">
                    Home
                </a>
                <a class="navbar-item" v-if="!this.isExpired" @click="$router.push('/paypal')">
                    Paypal
                </a>

                </div>

                <div class="navbar-end">
                <div class="navbar-item">
                    <div class="buttons" v-if="this.isExpired">
                        <button class="button is-primary" @click="$router.push('/registration')">
                            <strong>Sign up</strong>
                        </button>
                        <button class="button is-light" @click="$router.push('/')">
                            Log in
                        </button>
                    </div>
                    <div class="buttons" v-else>
                        <a class="navbar-item" @click="logout()">
                            {{this.username}}
                        </a>
                        <button class="button is-light" @click="logout()">
                            Logout
                        </button>
                    </div>
                </div>
                </div>
            </div>
            </nav>
    </div>
</template>

<script>

import { store } from "@/main";
import UserService from "../services/UserService";

export default {
    name: "NavbarComponent",
    data() {
        return {
            isExpired: false,
            username: ""
        };
    },
    mounted() {
        this.isExpired = UserService.isExpired();
        if (!this.isExpired) {
            this.username = UserService.getUsername();
        }
    },
    methods: {
        logout() {
            store.commit("setToken", null);
            store.commit("setWebShop", null);
            this.isExpired = UserService.isExpired();
            this.$router.push("/");
        }
    },
    components: { }
}

</script>