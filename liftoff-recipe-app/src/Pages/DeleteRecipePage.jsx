import TextField from "@mui/material/TextField";
import Box from "@mui/material/Box";
import Button from "@mui/material/Button";
import axios from "axios"


export default function DeleteRecipe() {
  const deleteRecipe = async (e: any) => {

    try {
          await axios.delete("http://localhost:8080/recipes/${e.target.id.value}")
          alert("Delete Successful")
        } catch(err) {
          alert("Delete Error! Check the console for more information")
          console.log('err',err)
        }
  };

 return(
    <Box>
    <h1>Delete Recipe</h1>
        <Box
            sx={{
                minHeight: 330,
                display: "flex",
                flexDirection: "column",
                justifyContent: "space-between",
                }}
                component = "form"
                onSubmit = {deleteRecipe}
        >
            <TextField id= "id" label="Recipe id" variant="outlined" />
            <Button type="submit" variant="contained">
                Delete
            </Button>
         </ Box>
    </ Box>
    );
    }