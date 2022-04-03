import { useRecipeComments } from "../recipe-queries"
import { List, ListItem, ListItemAvatar, ListItemText, ListSubheader, Typography } from "@mui/material"
import AccountCircleIcon from "@mui/icons-material/AccountCircle"

interface RecipeCommentListProps {
	recipeId: number
}

export const RecipeCommentList = ({ recipeId }: RecipeCommentListProps) => {
	const { recipeComments } = useRecipeComments(recipeId)

	return (
		<>
			{ recipeComments && recipeComments.length > 0 &&
				<List
					subheader={
						<ListSubheader>
							Comments
						</ListSubheader>
					}
				>
					{ recipeComments.map((comment, i) => (
						<ListItem key={i}>
							<ListItemAvatar>
								<AccountCircleIcon fontSize="large"/>
							</ListItemAvatar>
							<ListItemText
								primary={
									<Typography>{comment.user.firstName} {comment.user.lastName} • {comment.created.toLocaleString()}</Typography>
								}
								secondary={
									<Typography>{comment.text}</Typography>
								}
							/>
						</ListItem>
					))}
				</List>
			}
		</>
	)
}