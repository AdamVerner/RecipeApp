import { useFieldArray, useForm } from "react-hook-form"
import { yupResolver } from "@hookform/resolvers/yup"
import {
	Button,
	TextField,
	Dialog,
	DialogTitle,
	DialogContent,
	DialogProps,
	Stack,
	Grid,
	Typography,
	List,
	ListItem,
	IconButton,
	ListItemText, styled, Divider, ListSubheader
} from "@mui/material"
import { useSnackbar } from "notistack"
import { Close, Delete } from "@mui/icons-material"
import {
	useSaveRecipeForm
} from "../recipe-queries"
import { GroceryFormData, RecipeFormData, RecipeSchema } from "../recipe-schemas"
import { AddGroceryForm } from "./AddGroceryForm"

export interface RecipeCreateDialogProps extends DialogProps {
	handleClose(): void
}

export const RecipeCreateDialog = ({ handleClose, ...props }: RecipeCreateDialogProps) => {
	const { enqueueSnackbar } = useSnackbar()

	const {
		control,
		register,
		handleSubmit,
		formState: { errors }
	} = useForm<RecipeFormData>({ resolver: yupResolver(RecipeSchema) })

	const { fields, append, remove } = useFieldArray<RecipeFormData, "items">({
		control,
		name: "items"
	})

	const { saveRecipeFormAsync, isLoading: isSubmitting } = useSaveRecipeForm()

	const onSubmit = (data: RecipeFormData) => {
		saveRecipeFormAsync(data)
			.then(() => {
				enqueueSnackbar("Recipe created", { variant: "success" })
				handleClose()
			})
	}

	const handleAddItem = async (data: GroceryFormData) => {
		append(data)
	}

	return (
		<Dialog {...props} fullWidth maxWidth="md"   onClose={handleClose}>
			<DialogTitle>
				<Stack direction="row">
					<Typography sx={{ flexGrow: 1 }} component="div" variant="h6">Create recipe</Typography>
					<IconButton onClick={handleClose}>
						<Close/>
					</IconButton>
				</Stack>
			</DialogTitle>
			<DialogContent>
				<Grid container spacing={3} padding={1} sx={{ flexGrow: 1 }}>
					<Grid item container md={4} sm={12}>
						<FlexForm onSubmit={handleSubmit(onSubmit)} >
							<Stack spacing={3} sx={{ flexGrow: 1 }} justifyContent="space-between">
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
									disabled={isSubmitting}
									variant="contained"
								>
								Create
								</Button>
							</Stack>
						</FlexForm>
					</Grid>
					<Grid item md={4} sm={12} display="flex">
						<AddGroceryForm
							onSubmit={handleAddItem}
							isSubmitting={isSubmitting}
						/>
					</Grid>
					<Grid item md={4} sm={12} display="flex">
						<Stack direction="row" sx={{ flexGrow: 1 }}>
							<Divider orientation="vertical" flexItem />
							<List
								sx={{ flexGrow: 1 }}
								subheader={
									<ListSubheader>
										<Typography variant="h5">Ingredients</Typography>
									</ListSubheader>}
							>
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
										<ListItemText
											primary={`${field.grocery}: ${field.quantity} ${field.unit}`}
											secondary={field.category}
										/>
									</ListItem>
								)
								)}
							</List>
						</Stack>
					</Grid>
				</Grid>
			</DialogContent>
		</Dialog>
	)
}


const FlexForm = styled("form")`
	display: flex;
  	flex-grow: 1;
`