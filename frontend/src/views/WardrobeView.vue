<script setup>
    import { ref, onMounted } from 'vue'
    import { useAuthStore } from '../stores/auth'
    const authStore = useAuthStore()
    const skins = ref([])

    onMounted(() => {
        fetchWardrobe()
    })

const fetchWardrobe = async () => {
        try {
            const response = await fetch('http://localhost:8080/api/wardrobe', {
                method: 'GET',
                headers: {
                    'Authorization': 'Bearer ' + authStore.token
                },
            })
            const data = await response.json()
            console.log('Réponse du serveur :', data)
            skins.value = data
        } catch (error) {
            console.error('Erreur lors de la récupération de la garde-robe :', error)
        }
    }

</script>

<template>
    <v-container fluid class="pa-6">
        <div class="text-center mb-8">
            <h1 class="text-h3 font-weight-bold text-gradient mb-2">Ma Garde-Robe</h1>
            <p class="text-body-1 text-medium-emphasis">Ta collection personnelle de skins</p>
            <v-chip
                v-if="skins.length > 0"
                color="primary"
                variant="elevated"
                size="large"
                class="mt-4"
                prepend-icon="mdi-star"
            >
                {{ skins.length }} skin{{ skins.length > 1 ? 's' : '' }} collecté{{ skins.length > 1 ? 's' : '' }}
            </v-chip>
        </div>

        <!-- Empty state -->
        <v-card
            v-if="skins.length === 0"
            class="empty-state mx-auto pa-8"
            max-width="500"
        >
            <div class="text-center">
                <v-icon size="80" color="primary" class="mb-4">mdi-wardrobe-outline</v-icon>
                <h3 class="text-h5 mb-2">Ta garde-robe est vide</h3>
                <p class="text-body-1 text-medium-emphasis mb-6">
                    Commence ta collection en ajoutant des skins depuis la page des champions !
                </p>
                <v-btn
                    color="primary"
                    size="large"
                    variant="elevated"
                    to="/champions"
                    prepend-icon="mdi-magnify"
                >
                    Explorer les champions
                </v-btn>
            </div>
        </v-card>

        <v-row v-else justify="center">
            <v-col
                v-for="skin in skins"
                :key="skin.id"
                cols="6"
                sm="4"
                md="3"
                lg="2"
            >
                <v-card class="skin-card">
                    <div class="card-image-container">
                        <v-img
                            :src="skin.imageUrl"
                            :alt="skin.skinName"
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
                            <h3 class="skin-name">{{ skin.skinName }}</h3>
                            <p class="champion-name">{{ skin.championName }}</p>
                        </div>
                        <div class="owned-badge">
                            <v-icon size="small">mdi-check</v-icon>
                        </div>
                        <div class="card-glow"></div>
                    </div>
                </v-card>
            </v-col>
        </v-row>
    </v-container>
</template>

<style scoped>
    .text-gradient {
        background: linear-gradient(135deg, #c9aa71 0%, #f0e6d3 50%, #c9aa71 100%);
        -webkit-background-clip: text;
        -webkit-text-fill-color: transparent;
        background-clip: text;
    }

    .empty-state {
        background: rgba(201, 170, 113, 0.05);
        border: 1px dashed rgba(201, 170, 113, 0.3);
        border-radius: 16px;
    }

    .skin-card {
        border-radius: 12px;
        overflow: hidden;
        background: transparent;
        transition: all 0.4s cubic-bezier(0.25, 0.8, 0.25, 1);
        border: 1px solid rgba(201, 170, 113, 0.2);
    }

    .skin-card:hover {
        transform: translateY(-8px) scale(1.02);
        border-color: rgba(201, 170, 113, 0.6);
        box-shadow:
            0 20px 40px rgba(0, 0, 0, 0.4),
            0 0 30px rgba(201, 170, 113, 0.2);
    }

    .card-image-container {
        position: relative;
        overflow: hidden;
    }

    .card-image {
        transition: transform 0.5s ease;
    }

    .skin-card:hover .card-image {
        transform: scale(1.1);
    }

    .card-overlay {
        position: absolute;
        bottom: 0;
        left: 0;
        right: 0;
        height: 65%;
        background: linear-gradient(
            to top,
            rgba(0, 0, 0, 0.95) 0%,
            rgba(0, 0, 0, 0.6) 40%,
            transparent 100%
        );
        pointer-events: none;
    }

    .card-content {
        position: absolute;
        bottom: 0;
        left: 0;
        right: 0;
        padding: 16px;
        text-align: center;
    }

    .skin-name {
        font-size: 1rem;
        font-weight: 700;
        color: #fff;
        text-shadow: 0 2px 4px rgba(0, 0, 0, 0.5);
        margin-bottom: 4px;
        transition: color 0.3s ease;
    }

    .skin-card:hover .skin-name {
        color: #c9aa71;
    }

    .champion-name {
        font-size: 0.85rem;
        color: rgba(255, 255, 255, 0.7);
        font-weight: 500;
        margin: 0;
    }

    .owned-badge {
        position: absolute;
        top: 12px;
        right: 12px;
        width: 28px;
        height: 28px;
        background: linear-gradient(135deg, #c9aa71 0%, #a08050 100%);
        border-radius: 50%;
        display: flex;
        align-items: center;
        justify-content: center;
        color: #1a1a2e;
        box-shadow: 0 2px 8px rgba(0, 0, 0, 0.3);
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
            rgba(201, 170, 113, 0.1) 0%,
            transparent 50%,
            rgba(201, 170, 113, 0.1) 100%
        );
        transition: opacity 0.4s ease;
        pointer-events: none;
    }

    .skin-card:hover .card-glow {
        opacity: 1;
    }
</style>