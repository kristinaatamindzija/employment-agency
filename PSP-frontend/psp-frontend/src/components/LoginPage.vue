<template>
	<div class="container" style="height:11%">
		<div class="row">
			<div class="col-lg-3 col-md-2"></div>

			<div class="col-lg-6 col-md-8 login-box" >
				<form method="POST" @submit.prevent = "login()">
					<div class="forma container">
						<div class="col-lg-12 login-key">
							<BIconKey class="login-key" style="color: turquoise"></BIconKey>
						</div>
						<div class="col-lg-12 login-title">LOGIN</div><br>
						<div class="form-group">
							<label class="label" for="username"><b>Username</b></label>
							<input class="input is-primary" type="text" v-model="form.username" required>
						</div>
						<br>

						<div class="form-group">
							<label class="label" for="password"><b>Password</b></label>
							<input class="input is-primary" type="password" v-model="form.password" required>
						</div>
						<br>
						<button class="button is-primary" type="submit">Login</button>
						<br><br>
						<div class="container signin">
							<p>Don't have an account? <a @click="$router.push('/registration')" style="color: blue">Register</a>.</p><br>
						</div>
					</div>
				</form>
			</div>
		</div>
	</div>


</template>

<script>
import { store } from "@/main";
import "../CSS/login.css"
import { BIconKey } from "bootstrap-vue";
import UserService from "../services/UserService";

export default {
    name: "LoginPage",
    data() {
        return {
            form: {
                username: "",
                password: ""
            },
        };
    },
    methods: {
        login() {
            UserService.login(this.form.username, this.form.password).then((response)=>{
                if(response.data.jwt != null){
                    store.commit("setToken", response.data?.jwt);
                    store.commit("setWebShop", response.data?.webShop);
                    //this.$router.push("/payment");
                    window.location.href = "/payment";
                }
            }).catch(() => {
                UserService.swalError("Invalid username/password");
            });

        }
    },
    components: { BIconKey }
}

</script>