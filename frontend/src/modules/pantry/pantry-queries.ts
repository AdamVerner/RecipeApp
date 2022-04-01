import { useMutation, useQuery, useQueryClient } from "react-query"
import { deletePantryItem, getPantryItems, savePantryItem } from "./pantry-api"
import { useGetOrSaveGrocery, useGroceries } from "../recipes/recipe-queries"
import { PantryGroceryFormData } from "./pantry-schemas"
import { useMemo } from "react"
import { PantryItemDisplay } from "./pantry-models"

export const PANTRY_ITEMS_QUERY_KEY = "pantry"

export const usePantryItems = () => {
	const { data, ...rest } = useQuery(PANTRY_ITEMS_QUERY_KEY, getPantryItems)

	return { pantryItems: data ?? [], ...rest }
}

export const usePantryItemsDisplay = () => {
	const { pantryItems } = usePantryItems()
	const { groceries } = useGroceries()

	return useMemo<PantryItemDisplay[]>(() => {
		return pantryItems
			.map(item => {
				const grocery = groceries.find(grocery => grocery.id === item.grocery)

				return {
					id: item.id,
					grocery: item.grocery,
					name: grocery?.name ?? "",
					category: grocery?.category ?? "",
					quantity: item.quantity,
					unit: item.unit,
					expiration: item.expiration ? new Date(item.expiration) : undefined
				}
			})
	}, [pantryItems, groceries])
}

export const useSavePantryItem = () => {
	const client = useQueryClient()

	const { mutate, mutateAsync, ...rest } = useMutation(savePantryItem,
		{
			onSuccess: async () => {
				await client.invalidateQueries(PANTRY_ITEMS_QUERY_KEY)
			}
		}
	)

	return { savePantryItem: mutate, savePantryItemAsync: mutateAsync, ...rest }
}

export const useSavePantryItemForm = () => {
	const { getOrSaveGroceryAsync } = useGetOrSaveGrocery()
	const { savePantryItemAsync } = useSavePantryItem()

	const { mutate, mutateAsync, ...rest } = useMutation(async (data: PantryGroceryFormData) => {
		const result = await getOrSaveGroceryAsync(data)
		await savePantryItemAsync({ ...result, expiration: data.expiration })
	})

	return { savePantryItemForm: mutate, savePantryItemFormAsync: mutateAsync, ...rest }
}

export const useDeletePantryItem = () => {
	const client = useQueryClient()

	const { mutate, mutateAsync, ...rest } = useMutation(deletePantryItem,
		{
			onSuccess: async () => {
				await client.invalidateQueries(PANTRY_ITEMS_QUERY_KEY)
			}
		})

	return { deletePantryItem: mutate, deletePantryItemAsync: mutateAsync, ...rest }
}