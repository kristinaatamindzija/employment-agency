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

                </div>

                <div class="navbar-end">
                <div class="navbar-item">
                    <div class="buttons">
                    <a class="button is-primary" @click="$router.push('/registration')">
                        <strong>Sign up</strong>
                    </a>
                    <a class="button is-light" @click="$router.push('/')">
                        Log in
                    </a>
                    </div>
                </div>
                </div>
            </div>
            </nav>
    </div>
</template>

<script>

export default {
    name: 'NavbarComponent',
    data() {
        return {

        }
    },
    methods: {

    }
}

</script>