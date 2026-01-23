<script setup>
    import { ref, reactive, onMounted } from 'vue'
    import { useRoute } from 'vue-router'
    import { useAuthStore } from '../stores/auth'

    const route = useRoute()
    const authStore = useAuthStore()

    const champion = ref(null)
    const loading = ref(true)
    const error = ref(null)
    const wardrobeSkinIds = ref(new Set())

    // Snackbar state
    const snackbar = reactive({
        show: false,
        message: '',
        color: 'success'
    })

    onMounted(async () => {
        await Promise.all([fetchChampionDetails(), fetchWardrobe()])
    })

    const isInWardrobe = (skinId) => {
        return wardrobeSkinIds.value.has(skinId)
    }

    const fetchWardrobe = async () => {
        try {
            const response = await fetch('http://localhost:8080/api/wardrobe', {
                method: 'GET',
                headers: {
                    'Authorization': 'Bearer ' + authStore.token
                }
            })
            if (response.ok) {
                const data = await response.json()
                wardrobeSkinIds.value = new Set(data.map(item => item.skinId))
            }
        } catch (err) {
            console.error('Erreur lors de la récupération de la garde-robe :', err)
        }
    }

    const fetchChampionDetails = async () => {
        try {
            const championId = route.params.id
            const response = await fetch(`http://localhost:8080/api/skins/champions/${championId}`, {
                method: 'GET',
                headers: {
                    'Authorization': 'Bearer ' + authStore.token
                }
            })

            if (!response.ok) {
                throw new Error('Champion non trouvé')
            }

            const data = await response.json()
            champion.value = data
        } catch (err) {
            error.value = err.message
            console.error('Erreur lors de la récupération du champion :', err)
        } finally {
            loading.value = false
        }
    }

    const addToWardrobe = async (skinId) => {
        try {
            const response = await fetch('http://localhost:8080/api/wardrobe', {
                method: 'POST',
                headers: {
                    'Authorization': 'Bearer ' + authStore.token,
                    'Content-Type': 'application/json'
                },
                body: JSON.stringify({ skinId: skinId })
            })

            if (response.ok) {
                wardrobeSkinIds.value.add(skinId)
                snackbar.message = 'Skin ajouté à votre garde-robe !'
                snackbar.color = 'success'
                snackbar.show = true
            } else {
                snackbar.message = 'Erreur lors de l\'ajout du skin'
                snackbar.color = 'error'
                snackbar.show = true
            }
        } catch (err) {
            console.error('Erreur lors de l\'ajout à la garde-robe :', err)
            snackbar.message = 'Erreur lors de l\'ajout du skin'
            snackbar.color = 'error'
            snackbar.show = true
        }
    }

    const removeFromWardrobe = async (skinId) => {
        try {
            const response = await fetch(`http://localhost:8080/api/wardrobe/${skinId}`, {
                method: 'DELETE',
                headers: {
                    'Authorization': 'Bearer ' + authStore.token
                }
            })

            if (response.ok) {
                wardrobeSkinIds.value.delete(skinId)
                snackbar.message = 'Skin retiré de votre garde-robe'
                snackbar.color = 'success'
                snackbar.show = true
            } else {
                snackbar.message = 'Erreur lors du retrait du skin'
                snackbar.color = 'error'
                snackbar.show = true
            }
        } catch (err) {
            console.error('Erreur lors du retrait de la garde-robe :', err)
            snackbar.message = 'Erreur lors du retrait du skin'
            snackbar.color = 'error'
            snackbar.show = true
        }
    }
</script>

<template>
    <v-container fluid class="pa-6">
        <!-- Loading state -->
        <div v-if="loading" class="d-flex justify-center align-center" style="min-height: 400px;">
            <div class="text-center">
                <v-progress-circular
                    indeterminate
                    color="primary"
                    size="80"
                    width="6"
                />
                <p class="text-body-1 mt-4 text-medium-emphasis">Chargement des skins...</p>
            </div>
        </div>

        <!-- Error state -->
        <v-alert
            v-else-if="error"
            type="error"
            variant="tonal"
            class="mb-4"
            border="start"
            prominent
        >
            <template v-slot:prepend>
                <v-icon size="large">mdi-alert-circle</v-icon>
            </template>
            {{ error }}
        </v-alert>

        <!-- Champion details -->
        <div v-else-if="champion">
            <div class="text-center mb-8">
                <h1 class="text-h3 font-weight-bold text-gradient mb-3">{{ champion.name }}</h1>
                <v-chip
                    color="primary"
                    variant="elevated"
                    size="large"
                    class="px-4"
                    prepend-icon="mdi-hanger"
                >
                    {{ champion.skins?.length || 0 }} skins disponibles
                </v-chip>
            </div>

            <v-row justify="center">
                <v-col
                    v-for="skin in champion.skins"
                    :key="skin.id"
                    cols="12"
                    sm="6"
                    md="4"
                    lg="3"
                    xl="2"
                >
                    <v-lazy
                        :min-height="300"
                        :options="{ threshold: 0.1 }"
                        transition="fade-transition"
                    >
                        <v-card class="skin-card">
                            <div class="card-image-container">
                                <v-img
                                    :src="skin.imageUrl"
                                    :alt="skin.name"
                                    class="card-image"
                                >
                                    <template v-slot:placeholder>
                                        <div class="d-flex align-center justify-center fill-height">
                                            <v-progress-circular indeterminate color="primary" />
                                        </div>
                                    </template>
                                </v-img>
                                <div class="card-overlay"></div>
                                <div class="card-content">
                                    <h3 class="skin-name">{{ skin.name }}</h3>
                                    <v-btn
                                        v-if="!isInWardrobe(skin.id)"
                                        class="add-btn mt-3"
                                        variant="flat"
                                        block
                                        @click="addToWardrobe(skin.id)"
                                    >
                                        <v-icon start>mdi-plus</v-icon>
                                        Ajouter à ma garde-robe
                                    </v-btn>
                                    <v-btn
                                        v-else
                                        class="remove-btn mt-3"
                                        variant="flat"
                                        block
                                        @click="removeFromWardrobe(skin.id)"
                                    >
                                        <v-icon start>mdi-minus</v-icon>
                                        Retirer de ma garde-robe
                                    </v-btn>
                                </div>
                                <div class="card-glow"></div>
                            </div>
                        </v-card>
                    </v-lazy>
                </v-col>
            </v-row>
        </div>

        <!-- Snackbar for notifications -->
        <v-snackbar
            v-model="snackbar.show"
            :color="snackbar.color"
            :timeout="3000"
            location="bottom right"
            rounded="lg"
        >
            <div class="d-flex align-center">
                <v-icon class="mr-2">
                    {{ snackbar.color === 'success' ? 'mdi-check-circle' : 'mdi-alert-circle' }}
                </v-icon>
                {{ snackbar.message }}
            </div>
            <template v-slot:actions>
                <v-btn variant="text" size="small" @click="snackbar.show = false">
                    Fermer
                </v-btn>
            </template>
        </v-snackbar>
    </v-container>
</template>

<style scoped>
    .text-gradient {
        background: linear-gradient(135deg, #c9aa71 0%, #f0e6d3 50%, #c9aa71 100%);
        -webkit-background-clip: text;
        -webkit-text-fill-color: transparent;
        background-clip: text;
    }

    .skin-card {
        border-radius: 16px;
        overflow: hidden;
        background: transparent;
        transition: all 0.4s cubic-bezier(0.25, 0.8, 0.25, 1);
        border: 1px solid rgba(201, 170, 113, 0.15);
    }

    .skin-card:hover {
        transform: translateY(-10px);
        border-color: rgba(201, 170, 113, 0.5);
        box-shadow:
            0 25px 50px rgba(0, 0, 0, 0.5),
            0 0 40px rgba(201, 170, 113, 0.15);
    }

    .card-image-container {
        position: relative;
        overflow: hidden;
    }

    .card-image {
        transition: transform 0.6s ease;
    }

    .skin-card:hover .card-image {
        transform: scale(1.08);
    }

    .card-overlay {
        position: absolute;
        bottom: 0;
        left: 0;
        right: 0;
        height: 70%;
        background: linear-gradient(
            to top,
            rgba(0, 0, 0, 0.95) 0%,
            rgba(0, 0, 0, 0.7) 30%,
            rgba(0, 0, 0, 0.3) 60%,
            transparent 100%
        );
        pointer-events: none;
    }

    .card-content {
        position: absolute;
        bottom: 0;
        left: 0;
        right: 0;
        padding: 20px;
        text-align: center;
    }

    .skin-name {
        font-size: 1.15rem;
        font-weight: 700;
        color: #fff;
        text-shadow: 0 2px 8px rgba(0, 0, 0, 0.6);
        letter-spacing: 0.3px;
        margin-bottom: 4px;
        transition: color 0.3s ease;
    }

    .skin-card:hover .skin-name {
        color: #c9aa71;
    }

    .add-btn {
        background: linear-gradient(135deg, #c9aa71 0%, #a08050 100%);
        color: #1a1a2e;
        font-weight: 600;
        text-transform: none;
        letter-spacing: 0.3px;
        border-radius: 8px;
        transition: all 0.3s ease;
    }

    .add-btn:hover {
        background: linear-gradient(135deg, #d4ba82 0%, #c9aa71 100%);
        box-shadow: 0 4px 15px rgba(201, 170, 113, 0.4);
    }

    .remove-btn {
        background: linear-gradient(135deg, #e74c3c 0%, #c0392b 100%);
        color: #fff;
        font-weight: 600;
        text-transform: none;
        letter-spacing: 0.3px;
        border-radius: 8px;
        transition: all 0.3s ease;
    }

    .remove-btn:hover {
        background: linear-gradient(135deg, #ff6b5b 0%, #e74c3c 100%);
        box-shadow: 0 4px 15px rgba(231, 76, 60, 0.4);
    }

    .card-glow {
        position: absolute;
        top: 0;
        left: 0;
        right: 0;
        bottom: 0;
        opacity: 0;
        background: linear-gradient(
            135deg,
            rgba(201, 170, 113, 0.08) 0%,
            transparent 40%,
            transparent 60%,
            rgba(201, 170, 113, 0.08) 100%
        );
        transition: opacity 0.4s ease;
        pointer-events: none;
    }

    .skin-card:hover .card-glow {
        opacity: 1;
    }
</style>
