import { useEffect, useMemo, useState } from "react"
import * as yup from "yup"
import { useFieldArray, useForm, Controller } from "react-hook-form"
import { yupResolver } from "@hookform/resolvers/yup"
import {
	Button,
	TextField,
	Dialog,
	DialogTitle,
	DialogContent,
	DialogProps,
	Stack,
	Autocomplete,
	Grid,
	Typography,
	List,
	ListItem,
	IconButton,
	ListItemText
} from "@mui/material"
import { useRecipeStore } from "../recipe-store"
import { useSnackbar } from "notistack"
import { Close, Delete } from "@mui/icons-material"

export interface RecipeFormData {
	name: string
	instructions: string
	portions: number
	items: RecipeItemFormData[]

	newItem: RecipeItemFormData
}

interface RecipeItemFormData {
	quantityUnit: string
	value: number
	grocery: string
	category: string
}

export interface RecipeCreateDialogProps extends DialogProps {
	handleClose(): void
}

const RecipeItemSchema = yup.object().shape({
	quantityUnit: yup.string().required("Quantity unit is required"),
	value: yup.number()
		.required("Value is required")
		.typeError("You must enter a number")
		.moreThan(0, "Quantity must be higher than 0"),
	grocery: yup.string().required("Grocery is required"),
	category: yup.string().required("Category is required")
})

const RecipeSchema = yup.object().shape({
	name: yup.string()
		.required("Name is required"),
	instructions: yup.string()
		.required("Instructions are required"),
	portions: yup.number()
		.required("Portions are required")
		.min(1, "Recipe need to have at least one portion")
		.integer("Portions can't be fractional")
		.typeError("You must enter a number"),
	items: yup.array().of(RecipeItemSchema).optional(),
	newItem: RecipeItemSchema.optional()
})

export const RecipeCreateDialog = ({ handleClose, ...props }: RecipeCreateDialogProps) => {
	const [isBusy, setIsBusy] = useState(false)
	const recipeStore = useRecipeStore()
	const { enqueueSnackbar } = useSnackbar()

	const {
		control,
		register,
		handleSubmit,
		getValues,
		setValue,
		watch,
		resetField,
		trigger,
		formState: { errors }
	} = useForm<RecipeFormData>({ resolver: yupResolver(RecipeSchema), shouldUnregister: false })

	const { fields, append, remove } = useFieldArray<RecipeFormData, "items">({
		control,
		name: "items"
	})

	const onSubmit = (data: RecipeFormData) => {
		setIsBusy(true)

		recipeStore.createRecipe(data)
			.then(() => {
				enqueueSnackbar("Recipe created", { variant: "success" })
				handleClose()
			})
			.catch(console.error)
			.finally(() => setIsBusy(false))
	}

	const groceryWatch = watch("newItem.grocery")

	const selectedGrocery = useMemo(() => {
		if (!groceryWatch) {
			return undefined
		}

		return recipeStore.findGrocery(groceryWatch)
	}, [groceryWatch, recipeStore])

	useEffect(() => {
		if (selectedGrocery) {
			setValue("newItem.category", selectedGrocery.category)
		}
	}, [selectedGrocery, setValue])

	const handleAddItem = async () => {
		const valid = await trigger("newItem")

		if (!valid) {
			return
		}

		append({
			quantityUnit: getValues("newItem.quantityUnit"),
			value: Number(getValues("newItem.value")),
			category: getValues("newItem.category"),
			grocery: getValues("newItem.grocery")
		})

		resetField("newItem.quantityUnit")
		resetField("newItem.value")
		resetField("newItem.category")
		resetField("newItem.grocery")
	}

	return (
		<Dialog {...props} fullWidth maxWidth="md" onClose={handleClose}>
			<form onSubmit={handleSubmit(onSubmit)}>
				<DialogTitle>
					<Stack direction="row">
						<Typography sx={{ flexGrow: 1 }} component="div" variant="h6">Create recipe</Typography>
						<IconButton onClick={handleClose}>
							<Close/>
						</IconButton>
					</Stack>
				</DialogTitle>
				<DialogContent>
					<Grid container spacing={3} padding={1}>
						<Grid item md={4} sm={12}>
							<Stack spacing={3}>
								<TextField
									error={!!errors.name}
									helperText={errors.name?.message}
									{...register("name")}
									label="Recipe name"
								/>
								<TextField
									error={!!errors.portions}
									helperText={errors.portions?.message}
									type="number"
									{...register("portions")}
									label="Portions"
								/>
								<TextField
									error={!!errors.instructions}
									helperText={errors.instructions?.message}
									multiline
									rows={4}
									{...register("instructions")}
									label="Instructions"
								/>
								<Button
									type="submit"
									disabled={isBusy}
									variant="contained"
								>
									Create
								</Button>
							</Stack>
						</Grid>
						<Grid item md={4} sm={12}>
							<Typography variant="h5">Items</Typography>
							<List>
								{fields.map((field, index) => (
									<ListItem
										key={index}
										secondaryAction={
											<IconButton
												onClick={() => remove(index)}
												edge="end"
												aria-label="delete"
											>
												<Delete/>
											</IconButton>
										}
									>
										<ListItemText primary={`${field.grocery}: ${field.value} ${field.quantityUnit}`}
														  secondary={field.category}/>
									</ListItem>
								)
								)}
							</List>
						</Grid>
						<Grid item md={4} sm={12}>
							<Stack spacing={3}>
								<Controller
									control={control}
									name="newItem.quantityUnit"
									render={({ field: { onChange, value, ...props } }) => (
										<Autocomplete
											options={recipeStore.quantityUnits}
											renderInput={(params) => <TextField
												{...params}
												label="Quantity unit"
												error={!!errors.newItem?.quantityUnit}
												helperText={errors.newItem?.quantityUnit?.message}
											/>}
											value={value ?? ""}
											onChange={(_, value) => onChange(value)}
											{...props}
										/>
									)}
								/>

								<TextField
									type="number"
									label="Value"
									{...register("newItem.value")}
									error={!!errors.newItem?.value}
									helperText={errors.newItem?.value?.message}
								/>
								<Controller
									control={control}
									name="newItem.grocery"
									render={({ field: { onChange, value, ...props } }) => (
										<Autocomplete
											options={recipeStore.groceries.map(grocery => grocery.name)}
											// getOptionLabel={option => (option as Grocery).name}
											freeSolo
											autoSelect
											renderInput={(params) => <TextField
												{...params}
												label="Grocery"
												error={!!errors.newItem?.grocery}
												helperText={errors.newItem?.grocery?.message}
											/>}
											value={value ?? ""}
											onChange={(_, value) => onChange(value)}
											{...props}
										/>
									)}
								/>

								<Controller
									control={control}
									name="newItem.category"
									render={({ field: { onChange, value, ...props } }) => (
										<Autocomplete
											options={recipeStore.groceryCategories}
											disabled={selectedGrocery !== undefined}
											renderInput={(params) => <TextField
												{...params}
												label="Category"
												error={!!errors.newItem?.category}
												helperText={errors.newItem?.category?.message}
											/>}
											value={value ?? ""}
											onChange={(_, value) => onChange(value)}
											{...props}
										/>
									)}
								/>
								<Button onClick={handleAddItem}>
									Add item
								</Button>
							</Stack>
						</Grid>
					</Grid>
				</DialogContent>
			</form>
		</Dialog>
	)
}