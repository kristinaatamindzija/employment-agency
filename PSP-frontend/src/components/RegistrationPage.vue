<template>
    <div class="container">
        <div class="row">
            <div class="col-lg-3 col-md-2"></div>
            <div class="col-lg-6 col-md-8 login-box">
                <form id="registrationForm" method="POST" @submit.prevent="submitForm">
                    <div class="container">
                        <div class="col-lg-12 login-key">
                            <i class="bi bi-file-person" aria-hidden="true"></i>
                        </div>
                        <div class="col-lg-12 login-title">REGISTRACIJA</div><br>

                        <label class="col-sm-4 col-form-label" for="name"><b>Name</b></label>
                        <input name="nameInput" pattern="[a-zA-Z]+[a-zA-Z ]+" title="Enter letters only."
                            class="col-sm-4 col-form-control" type="text" v-model="form.currency" required>
                        <br>

                        <label class="col-sm-4 col-form-label" for="surname"><b>Surname</b></label>
                        <input pattern="[a-zA-Z]+[a-zA-Z ]+" title="Enter letters only."
                            class="col-sm-4 col-form-control" type="text" v-model="form.username" required>
                        <br>

                        <label class="col-sm-4 col-form-label" for="email"><b>Email</b></label>
                        <input class="col-sm-4 col-form-control" type="email" v-model="form.password" required>
                        <br>

                        <label class="col-sm-4 col-form-label" for="password"><b>Password</b></label>
                        <input id="password" class="col-sm-4 col-form-control" type="password" v-model="form.apiToken"
                            @change='check_pass()' required>
                        <br>

                        <label class="col-sm-4 col-form-label" for="address"><b>Address</b></label>
                        <input class="col-sm-4 col-form-control" type="text" v-model="form.successUrl" required>
                        <br>

                        <label class="col-sm-4 col-form-label" for="city"><b>City</b></label>
                        <input pattern="[a-zA-Z]+[a-zA-Z ]+" class="col-sm-4 col-form-control" type="text"
                            v-model="form.errorUrl" required>
                        <br>

                        <label class="col-sm-4 col-form-label" for="country"><b>Country</b></label>
                        <input pattern="[a-zA-Z]+[a-zA-Z ]+" title="Enter letters only."
                            class="col-sm-4 col-form-control" type="text" v-model="form.failureUrl" required>
                        <br>

                        <button id="submit" class="button" type="submit" disabled>Register</button>
                        <div class="container signin">
                            <p>Vec imate nalog? <a href="#/login">Sign in</a>.</p>
                        </div>
                    </div>
                </form>
            </div>
        </div>
    </div>
</template>

<script>

export default {
    name: 'RegistrationPage',
    data() {
        return {
            form: {
                currency: '',
                username: '',
                password: '',
                apiToken: '',
                successUrl: '',
                errorUrl: '',
                failureUrl: '',
            },
        }
    }
}

</script>