import { useSavePantryItemForm } from "../pantry-queries"
import { useForm } from "react-hook-form"
import { yupResolver } from "@hookform/resolvers/yup"
import { ControlledAutocomplete } from "../../forms/ControlledAutocomplete"
import { Button, DialogContent, DialogProps, Grid, TextField, Dialog } from "@mui/material"
import { PantryGroceryFormData, PantryGrocerySchema } from "../pantry-schemas"
import { useGroceries, useGrocery, useGroceryCategories, useQuantityUnits } from "../../recipes/recipe-queries"
import { useEffect } from "react"
import { useSnackbar } from "notistack"

export interface AddPantryGroceryFormProps extends DialogProps {
	handleClose(): void
}

export const AddPantryGroceryForm = ({ handleClose, ...props }: AddPantryGroceryFormProps) => {
	const { control, register, handleSubmit, watch, setValue, reset, trigger, formState: { errors } }
		= useForm<PantryGroceryFormData>({ resolver: yupResolver(PantryGrocerySchema) })

	const { groceries } = useGroceries()
	const { groceryCategories } = useGroceryCategories()
	const { quantityUnits } = useQuantityUnits()
	const { savePantryItemFormAsync, isLoading: isSubmitting } = useSavePantryItemForm()

	const { enqueueSnackbar } = useSnackbar()

	const groceryWatch = watch("grocery")

	const { grocery: selectedGrocery } = useGrocery(groceryWatch ?? "")

	useEffect(() => {
		if (selectedGrocery) {
			setValue("category", selectedGrocery.category)
			trigger("category")
		}
	}, [selectedGrocery, setValue, trigger])

	const onSubmit = (data: PantryGroceryFormData) => {
		savePantryItemFormAsync(data)
			.then(() => {
				enqueueSnackbar("Item saved", { variant: "success" })
				reset()
				handleClose()
			})
	}

	return (
		<Dialog {...props} fullWidth onClose={handleClose}>
			<DialogContent>
				<form onSubmit={handleSubmit(onSubmit)}>
					<Grid container spacing={2}>
						<Grid item container xs={12} spacing={2}>
							<Grid item xs={6}>
								<ControlledAutocomplete
									control={control}
									name="grocery"
									options={groceries.map(grocery => grocery.name)}
									freeSolo
									autoSelect
									renderInput={
										(params) =>
											<TextField
												{...params}
												label="Grocery"
												error={!!errors.grocery}
												helperText={errors.grocery?.message}
											/>
									}
								/>
							</Grid>
							<Grid item xs={6}>
								<ControlledAutocomplete
									control={control}
									name="category"
									options={groceryCategories}
									disabled={selectedGrocery !== undefined}
									renderInput={
										(params) =>
											<TextField
												{...params}
												label="Category"
												error={!!errors.category}
												helperText={errors.category?.message}
											/>
									}
								/>
							</Grid>
						</Grid>

						<Grid item container xs={12} spacing={2}>
							<Grid item xs={6}>
								<ControlledAutocomplete
									control={control}
									name="unit"
									options={quantityUnits}
									renderInput={
										(params) =>
											<TextField
												{...params}
												label="Unit"
												error={!!errors.unit}
												helperText={errors.unit?.message}
											/>
									}
								/>
							</Grid>
							<Grid item xs={6}>
								<TextField
									{...register("quantity")}
									type="number"
									label="Quantity"
									fullWidth
									error={!!errors.quantity}
									helperText={errors.quantity?.message}
								/>
							</Grid>
						</Grid>
						<Grid item container xs={12} spacing={2}>
							<Grid item xs={6}>
								<TextField
									{...register("expiration")}
									type="date"
									label="Expiration"
									InputLabelProps={{ shrink: true }}
									fullWidth
									error={!!errors.expiration}
									helperText={errors.expiration?.message}
								/>
							</Grid>
							<Grid item xs={6} justifyContent="flex-end" alignItems="center" display="flex">
								<Button
									type="submit"
									disabled={isSubmitting}
									variant="contained"
								>
									Save
								</Button>
							</Grid>
						</Grid>
					</Grid>
				</form>
			</DialogContent>
		</Dialog>

	)
}