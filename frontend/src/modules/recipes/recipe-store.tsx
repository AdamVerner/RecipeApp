import { Grocery, GroceryCategory, Recipe } from "./recipe-models"
import create from "zustand"
import {
	saveRecipe,
	SaveRecipeRequest,
	getGroceries,
	getGroceryCategories,
	getQuantityUnits,
	getAllRecipes,
	SaveGroceryRequest,
	saveGrocery,
	getUserRecipes
} from "./recipe-api"
import createContext from "zustand/context"
import { FC } from "react"
import { RecipeFormData } from "./components/RecipeCreateDialog"

export interface RecipeStore {
	allRecipes: Recipe[]
	userRecipes: Recipe[]
	groceryCategories: GroceryCategory[]
	groceries: Grocery[]
	quantityUnits: string[]
	initialized: boolean

	fetchAll(): Promise<void>
	fetchGroceryCategories(): Promise<void>
	fetchGroceries(): Promise<void>
	fetchQuantityUnits(): Promise<void>
	fetchRecipes(search?: string): Promise<void>

	createRecipe(recipe: RecipeFormData): Promise<Recipe>
	createGrocery(grocery: SaveGroceryRequest): Promise<Grocery>
	findGrocery(name: string): Grocery | undefined
}

const createRecipeStore = () =>
	create<RecipeStore>((set, get) => ({
		allRecipes: [],
		userRecipes: [],
		groceryCategories: [],
		groceries: [],
		quantityUnits: [],
		initialized: false,

		fetchAll: async () => {
			await get().fetchRecipes()
			await get().fetchGroceryCategories()
			await get().fetchGroceries()
			await get().fetchQuantityUnits()

			set({ initialized: true })
		},

		fetchRecipes: async () => {
			const allRecipes = await getAllRecipes()
			const userRecipes = await getUserRecipes()

			set({ allRecipes, userRecipes })
		},

		fetchQuantityUnits: async () => {
			const quantityUnits = await getQuantityUnits()

			set({ quantityUnits })
		},

		fetchGroceryCategories: async () => {
			const groceryCategories = await getGroceryCategories()

			set({ groceryCategories })
		},

		fetchGroceries: async () => {
			const groceries = await getGroceries()

			set({ groceries })
		},

		createRecipe: async (recipe) => {
			const items: {
					grocery: number
					unit: string
					quantity: number
				}[] = []

			for (const item of recipe.items) {
				const unit = item.quantityUnit
				const quantity = item.value

				const grocery = get().findGrocery(item.grocery)

				if (grocery) {
					items.push({
						grocery: grocery.id,
						unit,
						quantity
					})
				} else {
					const newGrocery = await get().createGrocery({ name: item.grocery, category: item.category })

					items.push({
						grocery: newGrocery.id,
						unit,
						quantity
					})
				}
			}

			const saveRequest: SaveRecipeRequest = {
				name: recipe.name,
				portions: recipe.portions,
				instructions: recipe.instructions,
				items
			}

			const result = await saveRecipe(saveRequest)

			set({
				allRecipes: [...get().allRecipes, result],
				userRecipes: [...get().userRecipes, result]
			})

			return result
		},

		createGrocery: async (grocery) => {
			const result = await saveGrocery(grocery)

			set({ groceries: [...get().groceries, result] })

			return result
		},

		findGrocery: (name: string) => {
			return get().groceries.find(grocery => grocery.name.toLowerCase() === name.toLowerCase())
		}
	})
	)

const RecipeStoreContext = createContext<RecipeStore>()

export const RecipeStoreProvider: FC = ({ children }) => {
	return (
		<RecipeStoreContext.Provider createStore={createRecipeStore}>
			{children}
		</RecipeStoreContext.Provider>
	)
}

export const useRecipeStore = RecipeStoreContext.useStore
