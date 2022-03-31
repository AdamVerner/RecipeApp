import { useForm } from "react-hook-form"
import { yupResolver } from "@hookform/resolvers/yup/dist/yup"
import { GroceryFormData, GrocerySchema } from "../recipe-schemas"
import { useGroceries, useGrocery, useGroceryCategories, useQuantityUnits } from "../recipe-queries"
import { useEffect } from "react"
import { ControlledAutocomplete } from "../../forms/ControlledAutocomplete"
import { Button, Stack, styled, TextField } from "@mui/material"

interface AddGroceryRecipeFormProps {
	onSubmit: (data: GroceryFormData) => void
	isSubmitting?: boolean
}

export const AddGroceryForm = ({ onSubmit: onSubmitCallback, isSubmitting }: AddGroceryRecipeFormProps) => {
	const { control, register, handleSubmit, setValue, watch, reset, trigger, formState: { errors }  }
		= useForm<GroceryFormData>({ resolver: yupResolver(GrocerySchema) })

	const { quantityUnits } = useQuantityUnits()
	const { groceryCategories } = useGroceryCategories()
	const { groceries } = useGroceries()

	const groceryWatch = watch("grocery")

	const { grocery: selectedGrocery } = useGrocery(groceryWatch ?? "")

	useEffect(() => {
		if (selectedGrocery) {
			setValue("category", selectedGrocery.category)
			trigger("category")
		}
	}, [trigger, selectedGrocery, setValue])

	const onSubmit = (data: GroceryFormData) => {
		reset()
		onSubmitCallback(data)
	}

	return (
		<FlexForm onSubmit={handleSubmit(onSubmit)}>
			<Stack spacing={3} sx={{ flexGrow: 1 }} justifyContent="space-between">
				<ControlledAutocomplete
					control={control}
					name="grocery"
					options={groceries?.map(grocery => grocery.name)}
					freeSolo
					autoSelect
					renderInput={
						(params) => <TextField
							{...params}
							label="Grocery"
							error={!!errors.grocery}
							helperText={errors.grocery?.message}
						/>
					}
				/>

				<ControlledAutocomplete
					options={groceryCategories}
					disabled={selectedGrocery !== undefined}
					name="category"
					control={control}
					renderInput={(params) =>
						<TextField
							{...params}
							label="Category"
							error={!!errors.category}
							helperText={errors.category?.message}
						/>
					}
				/>

				<ControlledAutocomplete
					control={control}
					name="unit"
					options={quantityUnits}
					renderInput={(params) =>
						<TextField
							{...params}
							label="Unit"
							error={!!errors.unit}
							helperText={errors.unit?.message}
						/>
					}
				/>

				<TextField
					type="number"
					label="Quantity"
					{...register("quantity")}
					error={!!errors.quantity}
					helperText={errors.quantity?.message}
				/>
				<Button
					type="submit"
					variant="outlined"
					disabled={isSubmitting}>
					Add ingredient
				</Button>
			</Stack>
		</FlexForm>
	)
}

const FlexForm = styled("form")`
	display: flex;
  	flex-grow: 1;
`