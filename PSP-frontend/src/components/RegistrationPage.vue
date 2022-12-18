<template>
    <div>
        <div class="row" style="margin-top: 3em;">
            <div class="col"></div>
            <div class="col">
                <h1 style="padding-left: 3em;">Registration</h1>
                <br>
                <b-form-group id="input-group-1" label="Username:" style="margin-top: 1em; margin-bottom: 2em;"
                    label-cols-sm="3" label-align-sm="right">
                    <b-form-input id="input-1" v-model="form.username" type="text" required></b-form-input>
                </b-form-group>
                <b-form-group id="input-group-2" label="Password:" style="margin-top: 1em; margin-bottom: 2em;"
                    label-cols-sm="3" label-align-sm="right">
                    <b-form-input id="input-2" v-model="form.password" type="password" required></b-form-input>
                </b-form-group>
                <b-form-group id="input-group-3" label="Currency:" style="margin-top: 1em; margin-bottom: 2em;"
                    label-cols-sm="3" label-align-sm="right">
                    <b-form-input id="input-3" v-model="form.currency" type="text" required></b-form-input>
                </b-form-group>
                <b-form-group id="input-group-4" label="ApiToken:" style="margin-top: 1em; margin-bottom: 2em;"
                    label-cols-sm="3" label-align-sm="right">
                    <b-form-input id="input-4" v-model="form.apiToken" type="text" required></b-form-input>
                </b-form-group>
                <b-form-group id="input-group-5" label="SuccessUrl:" style="margin-top: 1em; margin-bottom: 2em;"
                    label-cols-sm="3" label-align-sm="right">
                    <b-form-input id="input-5" v-model="form.successUrl" type="text" required></b-form-input>
                </b-form-group>
                <b-form-group id="input-group-6" label="ErrorUrl:" style="margin-top: 1em; margin-bottom: 2em;"
                    label-cols-sm="3" label-align-sm="right">
                    <b-form-input id="input-6" v-model="form.errorUrl" type="text" required></b-form-input>
                </b-form-group>
                <b-form-group id="input-group-7" label="FailureUrl:" style="margin-top: 1em; margin-bottom: 2em;"
                    label-cols-sm="3" label-align-sm="right">
                    <b-form-input id="input-7" v-model="form.failureUrl" type="text" required></b-form-input>
                </b-form-group>
                <b-button type="reset" @click="$router.push('/')" variant="primary" style="margin-left: 7em;">Back</b-button>
                <b-button type="submit" variant="primary" style="margin-left: 12em;">Register</b-button>
            </div>
            <div class="col"></div>
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