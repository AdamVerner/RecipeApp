import { useNavigate, useParams } from "react-router-dom"
import {
	Button,
	Card,
	List,
	ListItemText,
	Stack,
	Typography,
	ListItem,
	ListSubheader,
	Divider,
	Rating
} from "@mui/material"
import RestaurantIcon from "@mui/icons-material/Restaurant"
import KitchenIcon from "@mui/icons-material/Kitchen"
import { useMemo } from "react"
import { useRecipeStore } from "../recipe-store"
import styled from "@emotion/styled"

interface RecipeItemDetail {
	id: number
	name: string
	category: string
	quantity: number
	unit: string
}

export const RecipeDetail = () => {
	const { id } = useParams()
	const navigate = useNavigate()

	const recipeStore = useRecipeStore()
	const recipe = useMemo(() => {
		return recipeStore.allRecipes.find(recipe => recipe.id === Number(id))
	}, [id, recipeStore])

	const groceries = useMemo(() => {
		return recipe?.items.map(item => {
			const grocery = recipeStore.groceries.find(grocery => grocery.id === item.grocery)!

			const result: RecipeItemDetail = {
				id: item.grocery,
				quantity: item.quantity,
				unit: item.unit,
				name: grocery.name,
				category: grocery.category
			}

			return result
		}).filter(grocery => grocery !== undefined)
	}, [recipe, recipeStore])


	return (
		<>
			<Stack spacing={2}>
				<RecipeCard>
					<Stack spacing={2}>
						<Typography variant="h4">{recipe?.name}</Typography>
						<Rating />
						<Typography><RestaurantIcon/> Portions: {recipe?.portions}</Typography>
						<Divider/>
						<List
							dense
							subheader={<ListSubheader><KitchenIcon/> Ingredients</ListSubheader>}
						>
							{groceries?.map((grocery, i) => (
								<ListItem key={i}>
									<ListItemText primary={`${grocery?.name}: ${grocery?.quantity} ${grocery?.unit}`}/>
								</ListItem>
							))}
						</List>
						<Divider/>
						<Typography variant="h5">Instructions</Typography>
						<InstructionsTypography>{recipe?.instructions}</InstructionsTypography>
						<Button onClick={() => navigate(-1)}>Back</Button>
					</Stack>
				</RecipeCard>
			</Stack>
		</>
	)
}

const InstructionsTypography = styled(Typography)`
	white-space: pre-line;
`

const RecipeCard = styled(Card)`
  padding: 30px;
  width: 800px;
  align-self: center;
`