<script setup>
import { useAuthStore } from '../stores/auth'
import { ref } from 'vue'
import { useRouter } from 'vue-router'

const email = ref('')
const password = ref('')
const authStore = useAuthStore()
const router = useRouter()
const loading = ref(false)
const showPassword = ref(false)
const errorMessage = ref('')
const form = ref(null)

const emailRules = [
    v => !!v || 'L\'email est requis',
    v => /.+@.+\..+/.test(v) || 'L\'email doit être valide'
]

const passwordRules = [
    v => !!v || 'Le mot de passe est requis',
    v => v.length >= 6 || 'Le mot de passe doit contenir au moins 6 caractères'
]

const handleLogin = async () => {
    const { valid } = await form.value.validate()
    if (!valid) return

    loading.value = true
    errorMessage.value = ''

    try {
        const reponse = await fetch('http://localhost:8080/api/auth/login', {
            method: 'POST',
            headers: {
                'Content-Type': 'application/json'
            },
            body: JSON.stringify({
                email: email.value,
                password: password.value
            })
        })

        if (!reponse.ok) {
            throw new Error('Email ou mot de passe incorrect')
        }

        const data = await reponse.json()
        console.log('Réponse du serveur :', data)
        authStore.setToken(data.token)
        console.log('Token stocké :', authStore.token)
        router.push('/wardrobe')
    } catch (error) {
        console.error('Erreur lors de la connexion :', error)
        errorMessage.value = error.message || 'Une erreur est survenue lors de la connexion'
    } finally {
        loading.value = false
    }
}
</script>

<template>
    <v-container class="fill-height" fluid>
        <v-row align="center" justify="center">
            <v-col cols="12" sm="8" md="4">
                <v-card class="elevation-12" rounded="lg">
                    <v-toolbar color="primary" dark flat>
                        <v-toolbar-title>Connexion à LoLFit</v-toolbar-title>
                    </v-toolbar>

                    <v-card-text class="pa-6">
                        <v-alert
                            v-if="errorMessage"
                            type="error"
                            variant="tonal"
                            class="mb-4"
                            closable
                            @click:close="errorMessage = ''"
                        >
                            {{ errorMessage }}
                        </v-alert>

                        <v-form ref="form" @submit.prevent="handleLogin">
                            <v-text-field
                                v-model="email"
                                :rules="emailRules"
                                label="Email"
                                prepend-inner-icon="mdi-email"
                                type="email"
                                variant="outlined"
                                class="mb-3"
                                required
                            />

                            <v-text-field
                                v-model="password"
                                :rules="passwordRules"
                                label="Mot de passe"
                                prepend-inner-icon="mdi-lock"
                                :type="showPassword ? 'text' : 'password'"
                                :append-inner-icon="showPassword ? 'mdi-eye' : 'mdi-eye-off'"
                                @click:append-inner="showPassword = !showPassword"
                                variant="outlined"
                                class="mb-3"
                                required
                            />

                            <v-btn
                                type="submit"
                                color="primary"
                                size="large"
                                block
                                :loading="loading"
                                class="mt-4"
                            >
                                Se connecter
                            </v-btn>
                        </v-form>
                    </v-card-text>

                    <v-card-actions class="pa-4 pt-0">
                        <v-spacer />
                        <span class="text-body-2 text-medium-emphasis">
                            Pas encore de compte ?
                            <router-link to="/register" class="text-primary">
                                S'inscrire
                            </router-link>
                        </span>
                        <v-spacer />
                    </v-card-actions>
                </v-card>
            </v-col>
        </v-row>
    </v-container>
</template>

<style scoped>
.fill-height {
    min-height: 100vh;
}
</style>
